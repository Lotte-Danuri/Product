package com.lotte.danuri.product.controller.category;

import com.lotte.danuri.product.model.dto.CategoryFirstDto;

import com.lotte.danuri.product.service.category.CategoryService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/categories")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "카테고리 조회", notes = "대/중/소분류 모든 카테고리를 조회한다.")
    public ResponseEntity<?> getCategories() {

        List<CategoryFirstDto> categoryFirstList = categoryService.getCategories();
        return ResponseEntity.ok(categoryFirstList);
    }
}