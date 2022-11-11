package com.lotte.danuri.product.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class BrandCategoryDto {
    private Long brandId;
    private Long storeId;
    private Long categoryFirstId;
    private Long categorySecondId;
    private Long categoryThirdId;
}
