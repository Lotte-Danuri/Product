package com.lotte.danuri.product.repository;

import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByDeletedDateIsNull();

    List<Product> findAllByPriceBetweenAndCategoryThirdIdInAndStoreIdIn(Double minPrice, Double maxPrice, List<Long> categoryThirdId, List<Long> storeId);

    List<Product> findAllByIdIn(List<Long> productId);

    List<Product> findAllByProductCode(String productCode);

    List<Product> findAllByDeletedDateIsNullAndStoreId(Long storeId);

    Product findByIdAndDeletedDateIsNull(Long id);

    List<Product> findAllByProductCodeAndDeletedDateIsNull(String v);

    List<Product> findAllByStoreIdAndDeletedDateIsNull(Long stordId);

    List<Product> findAllByStoreIdAndCategoryFirstIdAndDeletedDateIsNull(Long stordId, Long categoryFirstId);

    List<Product> findAllByStoreIdAndCategorySecondIdAndDeletedDateIsNull(Long stordId, Long categorySecondId);

    List<Product> findAllByStoreIdAndCategoryThirdIdAndDeletedDateIsNull(Long stordId, Long categoryThirdId);
    List<Product> findAllByStoreIdAndProductNameContainingAndDeletedDateIsNull(Long stordId, String productName);

    List<Product> findAllByStoreIdAndProductNameContainingAndCategoryFirstIdAndDeletedDateIsNull(Long stordId, String productName, Long categoryFirstId);

    List<Product> findAllByStoreIdAndProductNameContainingAndCategorySecondIdAndDeletedDateIsNull(Long stordId, String productName, Long categorySecondId);

    List<Product> findAllByStoreIdAndProductNameContainingAndCategoryThirdIdAndDeletedDateIsNull(Long stordId, String productName, Long categoryThirdId);
}