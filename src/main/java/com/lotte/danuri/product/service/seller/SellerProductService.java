package com.lotte.danuri.product.service.seller;

import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.model.dto.ProductDto;

import java.util.List;

public interface SellerProductService {
    void createProduct(ProductDto productDto);

    void deleteProduct(Long id);

    void updateProduct(ProductDto productDto);

    List<ProductDto> getProducts();
}
