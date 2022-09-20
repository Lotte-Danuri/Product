package com.lotte.danuri.product.controller.buyer;

import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.service.buyer.BuyerProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
@RequiredArgsConstructor
@CrossOrigin("*")
public class BuyerProductController {
    private final BuyerProductService buyerProductService;

    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getProducts(){

        List<ProductDto> productList = buyerProductService.getProducts();
        return ResponseEntity.ok(productList);
    }

    @GetMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> getProduct(@PathVariable("productId") Long productId){

        ProductDto productDto = buyerProductService.getProduct(productId);
        return ResponseEntity.ok(productDto);
    }
}