package com.lotte.danuri.product.service.seller;

import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.CategoryNotFoundException;
import com.lotte.danuri.product.exception.ProductNotFoundException;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.CategoryFirst;
import com.lotte.danuri.product.model.entity.CategorySecond;
import com.lotte.danuri.product.model.entity.CategoryThird;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.repository.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerProductServiceImpl implements SellerProductService {

    private final ProductRepository productRepository;
    private final CategoryFirstRepository categoryFirstRepository;
    private final CategorySecondRepository categorySecondRepository;
    private final CategoryThirdRepository categoryThirdRepository;

    public ProductDto createProduct(ProductDto productDto){
        Optional<CategoryFirst> categoryFirst = categoryFirstRepository.findById(productDto.getCategoryFirstId());
        Optional<CategorySecond> categorySecond = categorySecondRepository.findById(productDto.getCategorySecondId());
        Optional<CategoryThird> categoryThird = categoryThirdRepository.findById(productDto.getCategoryThirdId());

        if (!categoryFirst.isPresent() || !categorySecond.isPresent() || !categoryThird.isPresent()){
            throw new CategoryNotFoundException("Category not present in the database", ErrorCode.CATEGORY_NOT_FOUND);
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        productDto.setLikeCount(0L);

        Product product = mapper.map(productDto, Product.class);
        product.setCategoryFirst(categoryFirst.get());
        product.setCategorySecond(categorySecond.get());
        product.setCategoryThird(categoryThird.get());

        productRepository.save(product);

        ProductDto returnValue = mapper.map(product, ProductDto.class);

        returnValue.setCategoryFirstId(categoryFirst.get().getId());
        returnValue.setCategorySecondId(categorySecond.get().getId());
        returnValue.setCategoryThirdId(categoryThird.get().getId());

        return returnValue;
    }

    public Iterable<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public void deleteProduct(ProductDto productDto){
        Optional<Product> optionalProduct = productRepository.findById(productDto.getId());

        if (!optionalProduct.isPresent()){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        optionalProduct.get().setDeletedDate(LocalDateTime.now());

        productRepository.save(optionalProduct.get());
    }

    public ProductDto updateProduct(ProductDto productDto){
        Optional<Product> optionalProduct = productRepository.findById(productDto.getId());
        Optional<CategoryFirst> categoryFirst = categoryFirstRepository.findById(productDto.getCategoryFirstId());
        Optional<CategorySecond> categorySecond = categorySecondRepository.findById(productDto.getCategorySecondId());
        Optional<CategoryThird> categoryThird = categoryThirdRepository.findById(productDto.getCategoryThirdId());

        if (!optionalProduct.isPresent() || optionalProduct.get().getDeletedDate() != null){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }

        if (!categoryFirst.isPresent() || !categorySecond.isPresent() || !categoryThird.isPresent() ||
            categoryFirst.get().getDeletedDate() != null || categorySecond.get().getDeletedDate() != null || categoryThird.get().getDeletedDate() != null){
            throw new CategoryNotFoundException("Category not present in the database", ErrorCode.CATEGORY_NOT_FOUND);
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        optionalProduct.get().setPrice(productDto.getPrice());
        optionalProduct.get().setProductName(productDto.getProductName());
        optionalProduct.get().setStock(productDto.getStock());
        optionalProduct.get().setStoreId(productDto.getStoreId());
        optionalProduct.get().setThumbnailUrl(productDto.getThumbnailUrl());
        optionalProduct.get().setCategoryFirst(categoryFirst.get());
        optionalProduct.get().setCategorySecond(categorySecond.get());
        optionalProduct.get().setCategoryThird(categoryThird.get());

        productRepository.save(optionalProduct.get());

        ProductDto returnValue = mapper.map(optionalProduct.get(), ProductDto.class);

        returnValue.setCategoryFirstId(categoryFirst.get().getId());
        returnValue.setCategorySecondId(categorySecond.get().getId());
        returnValue.setCategoryThirdId(categoryThird.get().getId());

        return productDto;
    }
}