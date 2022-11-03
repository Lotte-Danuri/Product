package com.lotte.danuri.product.service.coupon;

import com.lotte.danuri.product.model.dto.CouponDto;
import com.lotte.danuri.product.model.dto.request.CouponListDto;
import com.lotte.danuri.product.model.dto.response.CouponByStoreDto;
import com.lotte.danuri.product.model.entity.Coupon;
import org.apache.catalina.LifecycleState;

import java.util.List;

public interface CouponService {
    void createCoupon(CouponDto couponDto);

    List<CouponDto> getCoupons();

    void deleteCoupon(Long id);

    void updateCoupon(CouponDto couponDto);

    List<CouponDto> getCouponList(CouponListDto couponListDto);

    List<CouponByStoreDto> getCouponsByStoreId(Long storeId);

    List<CouponDto> getCouponDetailList(CouponListDto couponListDto);

    CouponByStoreDto getCouponDetail(Long couponId);
}
