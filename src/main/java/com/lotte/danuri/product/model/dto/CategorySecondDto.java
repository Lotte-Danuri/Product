package com.lotte.danuri.product.model.dto;

import com.lotte.danuri.product.model.entity.CategorySecond;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CategorySecondDto {

    private Long id;
    private String categoryName;

    private List<CategoryThirdDto> categoryThirdDtoList;

    public CategorySecondDto(CategorySecond categorySecond, List<CategoryThirdDto> categoryThirdDtoList) {
        this.id = categorySecond.getId();
        this.categoryName = categorySecond.getCategoryName();
        this.categoryThirdDtoList = categoryThirdDtoList;
    }
}
