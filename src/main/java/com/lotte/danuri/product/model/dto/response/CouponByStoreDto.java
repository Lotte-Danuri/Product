package com.lotte.danuri.product.model.dto.response;

import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CouponByStoreDto {

    private Long id;
    private Long storeId;
    private String name;
    private String contents;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;

    private Double discountRate;
    private Long minOrderPrice;
    private Long maxDiscountPrice;

    private List<ProductDto> productDtoList;

    public CouponByStoreDto(Coupon coupon, List<ProductDto> productDtoList) {
        this.id = coupon.getId();
        this.storeId = coupon.getStoreId();
        this.name = coupon.getName();
        this.contents = coupon.getContents();
        this.startDate = coupon.getStartDate();
        this.endDate = coupon.getEndDate();
        this.discountRate = coupon.getDiscountRate();
        this.minOrderPrice = coupon.getMinOrderPrice();
        this.maxDiscountPrice = coupon.getMaxDiscountPrice();
        this.productDtoList = productDtoList;
    }

    public CouponByStoreDto(Coupon coupon) {
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
}
