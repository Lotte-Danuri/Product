package com.lotte.danuri.product.controller.seller;

import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.service.seller.SellerProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.MediaType;
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

    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity createProduct (@RequestBody ProductDto productDto){

        sellerProductService.createProduct(productDto);
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getProducts(){

        List<ProductDto> productList = sellerProductService.getProducts();
        return ResponseEntity.ok(productList);
    }


    @DeleteMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity deleteProduct (@RequestBody ProductDto dto) {

        sellerProductService.deleteProduct(dto.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity updateProduct (@RequestBody ProductDto productDto){

        sellerProductService.updateProduct(productDto);
        return ResponseEntity.ok().build();
    }
}