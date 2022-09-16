package com.lotte.danuri.product.model.response;

import com.lotte.danuri.product.model.entity.CouponProduct;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponResponse {
    private Long id;
    private Long storeId;
    private String name;
    private String contents;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    private Double discountRate;
    private Double minOrderPrice;
    private Double maxDiscountPrice;

    private List<CouponProduct> couponProducts;
}
