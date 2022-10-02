package com.lotte.danuri.product.controller.seller;

import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.service.seller.SellerProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/sellers/products")
@RequiredArgsConstructor
@CrossOrigin("*")
public class SellerProductController {
    private final SellerProductService sellerProductService;

    @PostMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(value = "상품 등록", notes = "상품을 등록한다.")
    public ResponseEntity createProduct (@RequestPart ProductDto productDto, @RequestPart List<MultipartFile> imageList){

        sellerProductService.createProduct(productDto, imageList);
        return ResponseEntity.ok().build();
    }


    @GetMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 조회", notes = "모든 상품을 조회한다.")
    public ResponseEntity<?> getProducts(){

        List<ProductDto> productList = sellerProductService.getProducts();
        return ResponseEntity.ok(productList);
    }


    @DeleteMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "상품 삭제", notes = "하나의 상품을 삭제한다.")
    public ResponseEntity deleteProduct (@RequestBody ProductDto dto) {

        sellerProductService.deleteProduct(dto.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value = "", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(value = "상품 수정", notes = "하나의 상품을 수정한다.")
    public ResponseEntity updateProduct (@RequestPart ProductDto productDto, @RequestPart List<MultipartFile> imageList){

        sellerProductService.updateProduct(productDto, imageList);
        return ResponseEntity.ok().build();
    }
}