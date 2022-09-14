package com.lotte.danuri.product.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
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
