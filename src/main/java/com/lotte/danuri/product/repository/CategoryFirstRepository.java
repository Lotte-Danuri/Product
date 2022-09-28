package com.lotte.danuri.product.repository;

import com.lotte.danuri.product.model.entity.CategoryFirst;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryFirstRepository extends JpaRepository<CategoryFirst, Long> {
    List<CategoryFirst> findAllByDeletedDateIsNull();
}
