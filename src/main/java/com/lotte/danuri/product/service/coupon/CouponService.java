package com.lotte.danuri.product.service.coupon;

import com.lotte.danuri.product.model.dto.CouponDto;
import com.lotte.danuri.product.model.entity.Coupon;
import org.apache.catalina.LifecycleState;

import java.util.List;

public interface CouponService {
    void createCoupon(CouponDto couponDto);

    List<CouponDto> getCoupons();

    void deleteCoupon(Long id);

    void updateCoupon(CouponDto couponDto);
}
