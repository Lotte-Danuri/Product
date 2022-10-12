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
public class ProductByConditionDto {
    private List<Long> categoryThirdId;
    private List<Long> brandId;
    private Double minPrice;
    private Double maxPrice;
}