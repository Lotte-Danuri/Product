package com.lotte.danuri.product.model.request;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CouponRequest {

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
}
