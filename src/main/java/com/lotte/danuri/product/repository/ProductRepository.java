package com.lotte.danuri.product.repository;

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
}