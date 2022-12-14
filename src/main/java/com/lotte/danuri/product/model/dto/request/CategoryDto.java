package com.lotte.danuri.product.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CategoryDto {
    private Long stordId;
    private Long categoryFirstId;
    private Long categorySecondId;
    private Long categoryThirdId;
    private String productName;
}
