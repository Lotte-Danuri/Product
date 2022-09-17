package com.lotte.danuri.product.service.buyer;

import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.ProductNotFoundException;
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
        List<Product> products = productRepository.findAll();

        List<ProductDto> result = new ArrayList<>();

        products.forEach(v -> {
            ProductDto productDto = ProductDto.builder()
                    .id(v.getId())
                    .productName(v.getProductName())
                    .thumbnailUrl(v.getThumbnailUrl())
                    .price(v.getPrice())
                    .stock(v.getStock())
                    .storeId(v.getStoreId())
                    .likeCount(v.getLikeCount())
                    .categoryFirstId(v.getCategoryFirst().getId())
                    .categorySecondId(v.getCategorySecond().getId())
                    .categoryThirdId(v.getCategoryThird().getId())
                    .build();
            result.add(productDto);
        });
        return result;
    }

    public ProductDto getProduct(Long productId){
        Optional<Product> product = productRepository.findById(productId);
        if(product.isEmpty()){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }
        ProductDto productDto = new ProductDto(product.get());
        return productDto;
    }
}