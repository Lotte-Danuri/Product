package com.lotte.danuri.product.repository;

import com.lotte.danuri.product.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}
