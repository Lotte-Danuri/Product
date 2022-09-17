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
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class SellerProductServiceImpl implements SellerProductService {

    private final ProductRepository productRepository;
    private final CategoryFirstRepository categoryFirstRepository;
    private final CategorySecondRepository categorySecondRepository;
    private final CategoryThirdRepository categoryThirdRepository;

    public void createProduct(ProductDto productDto) {
        Optional<CategoryFirst> categoryFirst = categoryFirstRepository.findById(productDto.getCategoryFirstId());
        Optional<CategorySecond> categorySecond = categorySecondRepository.findById(productDto.getCategorySecondId());
        Optional<CategoryThird> categoryThird = categoryThirdRepository.findById(productDto.getCategoryThirdId());

        if (categoryFirst.isEmpty() || categorySecond.isEmpty() || categoryThird.isEmpty()){
            throw new CategoryNotFoundException("Category not present in the database", ErrorCode.CATEGORY_NOT_FOUND);
        }


        Product product = productRepository.save(
                Product.builder()
                        .categoryFirst(categoryFirst.get())
                        .categorySecond(categorySecond.get())
                        .categoryThird(categoryThird.get())
                        .productName(productDto.getProductName())
                        .thumbnailUrl(productDto.getThumbnailUrl())
                        .price(productDto.getPrice())
                        .stock(productDto.getStock())
                        .storeId(productDto.getStoreId())
                        .likeCount(0L)
                        .build()
        );
    }

    public List<ProductDto> getProducts(){
        List<Product> products = productRepository.findAll();
        List<ProductDto> result = new ArrayList<>();

        products.forEach(v -> {
            ProductDto productDto = new ProductDto(v);
            result.add(productDto);
        });
        return result;
    }

    public void deleteProduct(Long id) {
        if(productRepository.findById(id).isEmpty()){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }

        Product product = productRepository.findById(id).get();
        product.updateDeletedDate(LocalDateTime.now());
        productRepository.save(product);
    }

    public void updateProduct(ProductDto productDto) {
        Optional<Product> product = productRepository.findById(productDto.getId());
        Optional<CategoryFirst> categoryFirst = categoryFirstRepository.findById(productDto.getCategoryFirstId());
        Optional<CategorySecond> categorySecond = categorySecondRepository.findById(productDto.getCategorySecondId());
        Optional<CategoryThird> categoryThird = categoryThirdRepository.findById(productDto.getCategoryThirdId());

        if(product.isEmpty()){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }

        if (categoryFirst.isEmpty() || categorySecond.isEmpty() || categoryThird.isEmpty()){
            throw new CategoryNotFoundException("Category not present in the database", ErrorCode.CATEGORY_NOT_FOUND);
        }

        product.get().update(productDto, categoryFirst.get(), categorySecond.get(), categoryThird.get());

        productRepository.save(product.get());
    }
}