package com.lotte.danuri.product.service.buyer;

import com.lotte.danuri.product.client.MemberServiceClient;
import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.ProductNotFoundException;
import com.lotte.danuri.product.exception.ProductWasDeletedException;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.dto.request.ProductByConditionDto;
import com.lotte.danuri.product.model.dto.request.ProductListDto;
import com.lotte.danuri.product.model.dto.response.ProductDetailResponseDto;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.lotte.danuri.product.util.DeDuplication.deduplication;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuyerProductServiceImpl implements BuyerProductService{

    private final ProductRepository productRepository;
    private final MemberServiceClient memberServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public List<ProductDto> getProducts(){
        List<Product> products = productRepository.findAllByDeletedDateIsNull();
        List<ProductDto> ProductDtoList = new ArrayList<>();

        products.forEach(v -> {
            ProductDto productDto = new ProductDto(v);
            ProductDtoList.add(productDto);
        });

        List<ProductDto> result = deduplication(ProductDtoList, ProductDto::getProductCode);
        return result;
    }

    @Override
    public ProductDetailResponseDto getProduct(Long productId){
        log.info("Before Retrieve [getProduct] Method IN [Product-Service]");
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
        log.info("Before Call [getNames] Method IN [Product-Service]");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        String storeName = circuitBreaker.run(() -> memberServiceClient.getNames(product.get().getStoreId()),
                throwable -> "");
        log.info("After Call [getNames] Method IN [Product-Service]");
        ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(product.get(),imageList, storeName);
        log.info("After Retrieve [getProduct] Method IN [Product-Service]");
        return productDetailResponseDto;
    }

    @Override
    public List<ProductDto> getProductsByCondition(ProductByConditionDto productByConditionDto) {
        List<Long> brandId = productByConditionDto.getBrandId();
        List<Long> storeId = new ArrayList<>();

        brandId.forEach(v -> {
            log.info("Before Call [getStoreId] Method IN [Product-Service]");
            CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
            List<Long> storeIdList = circuitBreaker.run(() -> memberServiceClient.getStoreId(v),
                    throwable -> new ArrayList<>());
            log.info("Before After [getStoreId] Method IN [Product-Service]");
            storeIdList.forEach(w -> {
                storeId.add(w);
            });
        });

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

    @Override
    public List<ProductDto> getProductList(ProductListDto productListDto){
        List<Product> productList = new ArrayList<>();
        productListDto.getProductId().forEach(v -> {
            productList.add(productRepository.findById(v).get());
        });

        List<ProductDto> result = new ArrayList<>();

        productList.forEach(v -> {
            ProductDto productDto = new ProductDto(v);
            result.add(productDto);
        });

        return result;
    }

    @Override
    public List<ProductDetailResponseDto> getProductListByProductCode(String productCode){
        List<ProductDetailResponseDto> result = new ArrayList<>();

        List<Product> productList = productRepository.findAllByProductCode(productCode);
        productList.forEach(v -> {
            List<String> imageList = new ArrayList<>();
            v.getImages().forEach(w -> {
                imageList.add(w.getImageUrl());
            });

            log.info("Before Call [getNames] Method IN [Product-Service]");
            CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
            String storeName = circuitBreaker.run(() -> memberServiceClient.getNames(v.getStoreId()),
                    throwable -> "");
            log.info("After Call [getNames] Method IN [Product-Service]");
            ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(v,imageList, storeName);
            result.add(productDetailResponseDto);
        });

        return result;
    }
}