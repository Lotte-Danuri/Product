package com.lotte.danuri.product.service.buyer;

import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.dto.request.ProductByConditionDto;
import com.lotte.danuri.product.model.dto.response.ProductDetailResponseDto;
import com.lotte.danuri.product.model.entity.Product;

import java.util.List;

public interface BuyerProductService {
    List<ProductDto> getProducts();

    ProductDetailResponseDto getProduct(Long productId);

    List<ProductDto> getProductsByCondition(ProductByConditionDto productByConditionDto);
}
