package com.lotte.danuri.product.repository;

import com.lotte.danuri.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByDeletedDateIsNull();

    List<Product> findAllByPriceBetweenAndCategoryThirdIdInAndStoreIdIn(Double minPrice, Double maxPrice, List<Long> categoryThirdId, List<Long> storeId);
}