package com.lotte.danuri.product.repository;

import com.lotte.danuri.product.model.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    @Query(
            value = "SELECT distinct c FROM Coupon c JOIN FETCH c.couponProducts cp "
                    + "WHERE c.deletedDate is null "
                    + "AND cp.deletedDate is null "
    )
    List<Coupon> findAll();
    List<Coupon> findAllByDeletedDateIsNull();
}
