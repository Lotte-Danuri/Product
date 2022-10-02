package com.lotte.danuri.product.service.seller;

import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.model.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SellerProductService {
    void createProduct(ProductDto productDto, MultipartFile multipartFile);

    void deleteProduct(Long id);

    void updateProduct(ProductDto productDto, MultipartFile multipartFile);

    List<ProductDto> getProducts();

    String uploadImage(MultipartFile multipartFile);
}
