package com.lotte.danuri.product.service.seller;

import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.model.dto.ProductDto;

public interface SellerProductService {
    ProductDto createProduct(ProductDto productDto);
    Iterable<Product> getAllProducts();

    void deleteProduct(ProductDto productDto);

    ProductDto updateProduct(ProductDto productDto);
}
