package com.lotte.danuri.product.controller.seller;

import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.request.ProductRequest;
import com.lotte.danuri.product.model.response.ProductResponse;
import com.lotte.danuri.product.service.seller.SellerProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/sellers/products")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SellerProductController {
    private final SellerProductService sellerProductService;

    @PostMapping("")
    public ResponseEntity<ProductResponse> createProduct (@RequestBody ProductRequest request){

        ProductDto productDto = new ModelMapper().map(request, ProductDto.class);

        ProductDto createProduct = sellerProductService.createProduct(productDto);

        return ResponseEntity.ok().build();
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>> getProducts(){

        Iterable<Product> productList = sellerProductService.getAllProducts();

        List<ProductResponse> result = new ArrayList<>();
        productList.forEach(v -> {
            result.add(new ModelMapper().map(v, ProductResponse.class));
        });

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("")
    public ResponseEntity<ProductResponse> deleteProduct (@RequestBody ProductRequest request) {

        ProductDto productDto = new ModelMapper().map(request, ProductDto.class);

        sellerProductService.deleteProduct(productDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("")
    public ResponseEntity<ProductResponse> updateProduct (@RequestBody ProductRequest request){

        ProductDto productDto = new ModelMapper().map(request, ProductDto.class);

        ProductDto updateDto = sellerProductService.updateProduct(productDto);

        return ResponseEntity.ok().build();
    }
}