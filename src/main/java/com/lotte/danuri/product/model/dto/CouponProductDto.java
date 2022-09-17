package com.lotte.danuri.product.model.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CouponProductDto {

    private Long id;
    private Long couponId;
    private Long productId;
}
