package com.lotte.danuri.product.model.dto;

import lombok.Data;

@Data
public class CouponProductDto {

    private Long id;
    private Long couponId;
    private Long productId;
}
