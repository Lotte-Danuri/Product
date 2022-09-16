package com.lotte.danuri.product.service.coupon;

import com.lotte.danuri.product.model.dto.CouponDto;
import com.lotte.danuri.product.model.entity.Coupon;

public interface CouponService {
    CouponDto createCoupon(CouponDto couponDto);

    Iterable<Coupon> getAllCoupons();

    void deleteCoupon(CouponDto couponDto);

    void updateCoupon(CouponDto couponDto);
}
