package com.lotte.danuri.product.service.buyer;

import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.Product;

public interface BuyerProductService {
    Iterable<Product> getAllProducts();

    ProductDto getProduct(Long productId);
}
