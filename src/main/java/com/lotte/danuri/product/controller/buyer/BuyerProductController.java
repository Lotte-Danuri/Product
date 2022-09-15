package com.lotte.danuri.product.controller.buyer;

import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.model.response.ProductResponse;
import com.lotte.danuri.product.service.buyer.BuyerProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
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

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>> getProducts(){

        Iterable<Product> productList = buyerProductService.getAllProducts();

        List<ProductResponse> result = new ArrayList<>();
        productList.forEach(v -> {
            result.add(new ModelMapper().map(v, ProductResponse.class));
        });

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("productId") Long productId){

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductDto productDto = buyerProductService.getProduct(productId);

        ProductResponse productResponse = mapper.map(productDto, ProductResponse.class);

        return ResponseEntity.ok(productResponse);
    }
}