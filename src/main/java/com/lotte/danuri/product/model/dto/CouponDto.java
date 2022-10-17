package com.lotte.danuri.product.model.dto;

import com.lotte.danuri.product.model.entity.Coupon;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CouponDto {

    private Long id;
    private Long storeId;
    private String name;
    private String contents;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private Double discountRate;
    private Double minOrderPrice;
    private Double maxDiscountPrice;

    private List<Long> productId;

    public CouponDto(Coupon coupon, List<Long> productId) {
        this.id = coupon.getId();
        this.storeId = coupon.getStoreId();
        this.name = coupon.getName();
        this.contents = coupon.getContents();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.discountRate = coupon.getDiscountRate();
        this.minOrderPrice = coupon.getMinOrderPrice();
        this.maxDiscountPrice = coupon.getMaxDiscountPrice();
        this.productId = productId;
    }

    public CouponDto(Coupon coupon) {
        this.id = coupon.getId();
        this.storeId = coupon.getStoreId();
        this.name = coupon.getName();
        this.contents = coupon.getContents();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.discountRate = coupon.getDiscountRate();
        this.minOrderPrice = coupon.getMinOrderPrice();
        this.maxDiscountPrice = coupon.getMaxDiscountPrice();
    }

    public void updateProductId(List<Long> couponProductId) {
        productId = couponProductId;
    }
}
