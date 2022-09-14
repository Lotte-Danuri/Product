package com.lotte.danuri.product.repository;

import com.lotte.danuri.product.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}