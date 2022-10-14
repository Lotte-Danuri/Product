package com.lotte.danuri.product.service.category;

import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.ProductNotFoundException;
import com.lotte.danuri.product.exception.ProductWasDeletedException;
import com.lotte.danuri.product.model.dto.CategoryFirstDto;
import com.lotte.danuri.product.model.dto.CategorySecondDto;
import com.lotte.danuri.product.model.dto.CategoryThirdDto;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.CategoryFirst;
import com.lotte.danuri.product.model.entity.CategorySecond;
import com.lotte.danuri.product.model.entity.CategoryThird;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.repository.CategoryFirstRepository;
import com.lotte.danuri.product.repository.CategorySecondRepository;
import com.lotte.danuri.product.repository.CategoryThirdRepository;
import com.lotte.danuri.product.repository.ProductRepository;
import com.lotte.danuri.product.service.buyer.BuyerProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryFirstRepository categoryFirstRepository;

    @Override
    public List<CategoryFirstDto> getCategories() {
        List<CategoryFirst> categoryFirsts = categoryFirstRepository.findAllByDeletedDateIsNull();
        List<CategoryFirstDto> CategoryFirstDtos = new ArrayList<>();
        categoryFirsts.forEach(v1-> {
            List<CategorySecond> categorySeconds = v1.getCategorySeconds();
            List<CategorySecondDto> categorySecondDtos = new ArrayList<>();
            categorySeconds.forEach(v2 ->{
                List<CategoryThird> categoryThirds = v2.getCategoryThirds();
                List<CategoryThirdDto> categoryThirdDtos= new ArrayList<>();
                categoryThirds.forEach(v3 -> {
                    CategoryThirdDto categoryThirdDto = new CategoryThirdDto(v3);
                    categoryThirdDtos.add(categoryThirdDto);
                });
                CategorySecondDto categorySecondDto = new CategorySecondDto(v2, categoryThirdDtos);
                categorySecondDtos.add(categorySecondDto);
            });
            CategoryFirstDto categoryFirstDto = new CategoryFirstDto(v1, categorySecondDtos);
            CategoryFirstDtos.add(categoryFirstDto);
        });
        return CategoryFirstDtos;
    }
}