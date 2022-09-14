package com.lotte.danuri.product.service;

import com.lotte.danuri.product.model.Product;
import com.lotte.danuri.product.model.dto.ProductDto;

public interface ProductService {
    ProductDto createProduct(ProductDto productDto);
    Iterable<Product> getAllProducts();

    void deleteProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);
}
