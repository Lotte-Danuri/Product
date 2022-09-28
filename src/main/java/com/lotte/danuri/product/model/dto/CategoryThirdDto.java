package com.lotte.danuri.product.model.dto;

import com.lotte.danuri.product.model.entity.CategoryThird;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CategoryThirdDto {

    private Long id;
    private String categoryName;

    public CategoryThirdDto(CategoryThird categoryThird) {
        this.id = categoryThird.getId();
        this.categoryName = categoryThird.getCategoryName();
    }
}
