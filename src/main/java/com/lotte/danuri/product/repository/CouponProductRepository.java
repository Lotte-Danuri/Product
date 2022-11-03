package com.lotte.danuri.product.repository;

import com.lotte.danuri.product.model.entity.Coupon;
import com.lotte.danuri.product.model.entity.CouponProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CouponProductRepository extends JpaRepository<CouponProduct, Long> {
    Optional<Iterable<CouponProduct>> findByCouponId(long id);

    Optional<Iterable<CouponProduct>> findByCouponIdAndDeletedDateIsNull(long id);

    List<CouponProduct> findAllByProductIdAndCouponIdInAndDeletedDateIsNull(Long productId, List<Long> couponId);
}
