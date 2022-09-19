package com.lotte.danuri.product.service.buyer;

import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.ProductNotFoundException;
import com.lotte.danuri.product.exception.ProductWasDeletedException;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.CategoryFirst;
import com.lotte.danuri.product.model.entity.CategorySecond;
import com.lotte.danuri.product.model.entity.CategoryThird;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

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

    public ProductDto getProduct(Long productId){
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

        ProductDto productDto = new ProductDto(product.get());
        return productDto;
    }
}