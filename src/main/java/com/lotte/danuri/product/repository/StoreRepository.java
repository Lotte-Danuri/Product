package com.lotte.danuri.product.repository;

import com.lotte.danuri.product.model.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
}
