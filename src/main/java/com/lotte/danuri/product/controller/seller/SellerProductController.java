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
    private final SellerProductService productService;

    @PostMapping("")
    public ResponseEntity<ProductResponse> createProduct (@RequestBody ProductRequest request){

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductDto productDto = mapper.map(request, ProductDto.class);

        ProductDto createProduct = productService.createProduct(productDto);
        ProductResponse productResponse = mapper.map(createProduct, ProductResponse.class);

        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("")
    public ResponseEntity<List<ProductResponse>> getProducts(){

        Iterable<Product> productList = productService.getAllProducts();

        List<ProductResponse> result = new ArrayList<>();
        productList.forEach(v -> {
            result.add(new ModelMapper().map(v, ProductResponse.class));
        });

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("")
    public ResponseEntity<ProductResponse> deleteProduct (@RequestBody ProductRequest request) {

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductDto productDto = mapper.map(request, ProductDto.class);

        productService.deleteProduct(productDto);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("")
    public ResponseEntity<ProductResponse> updateProduct (@RequestBody ProductRequest request){

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductDto productDto = mapper.map(request, ProductDto.class);

        ProductDto updateDto = productService.updateProduct(productDto);
        ProductResponse productResponse = mapper.map(updateDto, ProductResponse.class);

        return ResponseEntity.ok(productResponse);
    }
}
