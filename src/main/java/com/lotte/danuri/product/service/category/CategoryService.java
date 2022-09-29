package com.lotte.danuri.product.service.category;

import com.lotte.danuri.product.model.dto.CategoryFirstDto;
import com.lotte.danuri.product.model.dto.ProductDto;

import java.util.List;

public interface CategoryService {
    List<CategoryFirstDto> getCategories();
}
