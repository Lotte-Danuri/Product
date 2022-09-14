package com.lotte.danuri.product.model.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private Long categoryFirstId;
    private Long categorySecondId;
    private Long categoryThirdId;

    private String productName;
    private String thumbnailUrl;
    private Double price;
    private Long stock;

    private Long storeId;
    private Long likeCount;
}