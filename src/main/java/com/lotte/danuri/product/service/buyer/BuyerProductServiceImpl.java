package com.lotte.danuri.product.service.buyer;

import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.ProductNotFoundException;
import com.lotte.danuri.product.exception.ProductWasDeletedException;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.dto.request.ProductByConditionDto;
import com.lotte.danuri.product.model.dto.response.ProductDetailResponseDto;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lotte.danuri.product.util.DeDuplication.deduplication;

@Service
@RequiredArgsConstructor
public class BuyerProductServiceImpl implements BuyerProductService{

    private final ProductRepository productRepository;
    public List<ProductDto> getProducts(){
        List<Product> products = productRepository.findAllByDeletedDateIsNull();
        List<ProductDto> result = new ArrayList<>();

        products.forEach(v -> {
            ProductDto productDto = new ProductDto(v);
            result.add(productDto);
        });
        return result;
    }

    public ProductDetailResponseDto getProduct(Long productId){
        Optional<Product> product = productRepository.findById(productId);

        // 예외 처리
        // 1. 상품이 DB에 존재하지 않을 경우
        if(product.isEmpty()){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 2. 상품이 삭제된 경우
        if(product.get().getDeletedDate() != null){
            throw new ProductWasDeletedException("Product was deleted in the database", ErrorCode.PRODUCT_WAS_DELETED);
        }

        List<String> imageList = new ArrayList<>();
        product.get().getImages().forEach(v -> {
            imageList.add(v.getImageUrl());
        });
        ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(product.get(),imageList);
        return productDetailResponseDto;
    }

    public List<ProductDto> getProductsByCondition(ProductByConditionDto productByConditionDto) {
        //TODO : brandId를 통해서 storeId LIST 를 불러오는 API 호출
        List<Long> storeId = new ArrayList<>();
        storeId.add(1L);
        storeId.add(2L);

        List<Product> productList = productRepository.findAllByPriceBetweenAndCategoryThirdIdInAndStoreIdIn(
                productByConditionDto.getMinPrice(),
                productByConditionDto.getMaxPrice(),
                productByConditionDto.getCategoryThirdId(),
                storeId);

        List<ProductDto> productDtoList = new ArrayList<>();

        productList.forEach(v -> {
            ProductDto productDto = new ProductDto(v);
            productDtoList.add(productDto);
        });

        List<ProductDto> result = deduplication(productDtoList, ProductDto::getProductCode);
        return result;
    }
}