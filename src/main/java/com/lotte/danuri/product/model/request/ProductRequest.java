package com.lotte.danuri.product.model.request;

import lombok.Data;

@Data
public class ProductRequest {
    private Long id;
    private Long categoryFirstId;
    private Long categorySecondId;
    private Long categoryThirdId;

    private String productName;
    private String thumbnailUrl;
    private Double price;
    private Long stock;

    private Long storeId;
}
