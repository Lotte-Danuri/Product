package com.lotte.danuri.product.model.dto;

import com.lotte.danuri.product.model.entity.CategoryFirst;
import com.lotte.danuri.product.model.entity.Coupon;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CategoryFirstDto {

    private Long id;
    private String categoryName;

    private List<CategorySecondDto> categorySecondDtoList;

    public CategoryFirstDto(CategoryFirst categoryFirst, List<CategorySecondDto> categorySecondDtoList) {
        this.id = categoryFirst.getId();
        this.categoryName = categoryFirst.getCategoryName();
        this.categorySecondDtoList = categorySecondDtoList;
    }
}
