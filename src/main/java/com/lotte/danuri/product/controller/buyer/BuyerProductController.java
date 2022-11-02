package com.lotte.danuri.product.controller.buyer;

import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.dto.request.ProductByConditionDto;
import com.lotte.danuri.product.model.dto.request.ProductListByCodeDto;
import com.lotte.danuri.product.model.dto.request.ProductListDto;
import com.lotte.danuri.product.model.dto.response.ProductDetailResponseDto;
import com.lotte.danuri.product.service.buyer.BuyerProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/condition", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 조건 조회", notes = "최소/최대 가격, 카테고리, 브랜드 조건에 의해 조회한다.")
    public ResponseEntity<?> getProductsByCondition(@RequestBody ProductByConditionDto productByConditionDto){

        List<ProductDto> productList = buyerProductService.getProductsByCondition(productByConditionDto);
        return ResponseEntity.ok(productList);
    }

    @PostMapping(value = "/list", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 리스트 조회", notes = "상품 Code 리스트에 의해 상품 리스트를 조회한다.")
    public ResponseEntity<?> getProductList(@RequestBody ProductListByCodeDto productListByCodeDto){

        List<ProductDto> productList = buyerProductService.getProductList(productListByCodeDto);
        return ResponseEntity.ok(productList);
    }

    @GetMapping(value = "/list/{productCode}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 코드에 의한 상품 상세 리스트 조회", notes = "상품 코드에 의해 상품 상세 리스트를 조회한다.")
    public ResponseEntity<?> getProductListByProductCode(@PathVariable("productCode") String productCode){

        List<ProductDetailResponseDto> ProductDetailResponseDto = buyerProductService.getProductListByProductCode(productCode);
        return ResponseEntity.ok(ProductDetailResponseDto);
    }
}