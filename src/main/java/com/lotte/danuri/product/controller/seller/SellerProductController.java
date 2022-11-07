package com.lotte.danuri.product.controller.seller;

import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.dto.request.CategoryDto;
import com.lotte.danuri.product.model.dto.request.ProductChanceDto;
import com.lotte.danuri.product.model.dto.response.SellerProductResponseDto;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.service.seller.SellerProductService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping(value = "/update", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ApiOperation(value = "상품 수정", notes = "하나의 상품을 수정한다.")
    public ResponseEntity updateProduct (@RequestPart ProductDto productDto, @RequestPart List<MultipartFile> imageList){

        sellerProductService.updateProduct(productDto, imageList);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{storeId}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "스토어의 상품 리스트 조회", notes = "스토어 Id에 의해 구매 전환율을 포함한 상품 목록을 조회한다.")
    public ResponseEntity<?> getProductsByStoreId(@PathVariable("storeId") Long storeId){

        List<SellerProductResponseDto> sellerProductResponseDto= sellerProductService.getProductsByStoreId(storeId);
        return ResponseEntity.ok(sellerProductResponseDto);
    }

    @PostMapping(value = "/category", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "카테고리에 따른 상품 조회", notes = "카테고리 대,중,소에 의해 상품 목록을 조회한다.")
    public ResponseEntity<?> getProductsByCategory(@RequestBody CategoryDto categoryDto){

        List<SellerProductResponseDto> sellerProductResponseDto = sellerProductService.getProductsByCategory(categoryDto);
        return ResponseEntity.ok(sellerProductResponseDto);
    }

    @PostMapping(value = "/chance", produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiOperation(value = "매출 기회 상위 상품 조회", notes = "주문이 5이하인 상품 뽑아서 전환율로 오름차순해 상위 8개 품목을 뽑는다.")
    public ResponseEntity<?> getProductChance(@RequestBody ProductChanceDto productChanceDto){
        List<SellerProductResponseDto> sellerProductResponseDto= sellerProductService.getProductChance(productChanceDto);
        return ResponseEntity.ok(sellerProductResponseDto);
    }
}