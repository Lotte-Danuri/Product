package com.lotte.danuri.product.controller.buyer;

import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.dto.response.ProductDetailResponseDto;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.service.buyer.BuyerProductService;
import io.swagger.annotations.ApiOperation;
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
    @ApiOperation(value = "상품 조회", notes = "모든 상품을 조회한다.")
    public ResponseEntity<?> getProducts(){

        List<ProductDto> productList = buyerProductService.getProducts();
        return ResponseEntity.ok(productList);
    }

    @GetMapping(value = "/{productId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 상세 조회", notes = "하나의 상품을 상세 조회한다.")
    public ResponseEntity<?> getProduct(@PathVariable("productId") Long productId){

        ProductDetailResponseDto ProductDetailResponseDto = buyerProductService.getProduct(productId);
        return ResponseEntity.ok(ProductDetailResponseDto);
    }
}