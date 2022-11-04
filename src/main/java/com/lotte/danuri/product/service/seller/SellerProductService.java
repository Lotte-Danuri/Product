package com.lotte.danuri.product.service.seller;

import com.lotte.danuri.product.model.dto.request.CategoryDto;
import com.lotte.danuri.product.model.dto.request.ProductChanceDto;
import com.lotte.danuri.product.model.dto.request.ProductListDto;
import com.lotte.danuri.product.model.dto.response.SellerProductResponseDto;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.model.dto.ProductDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface SellerProductService {
    void createProduct(ProductDto productDto, List<MultipartFile> multipartFile);

    void deleteProduct(Long id);

    void updateProduct(ProductDto productDto, List<MultipartFile> multipartFile);

    List<ProductDto> getProducts();

    String uploadImage(MultipartFile multipartFile);

    List<SellerProductResponseDto> getProductsByStoreId(Long storeId);

    List<ProductDto> getProductsByCategory(CategoryDto categoryDto);

    List<SellerProductResponseDto> getProductChance(ProductChanceDto productChanceDto);
}
