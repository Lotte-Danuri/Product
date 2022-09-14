package com.lotte.danuri.product.service.buyer;

import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.ProductNotFoundException;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuyerProductServiceImpl implements BuyerProductService{
    private final ProductRepository productRepository;
    public Iterable<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public ProductDto getProduct(Long productId){
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (!optionalProduct.isPresent()){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProductDto productDto = mapper.map(optionalProduct.get(), ProductDto.class);

        productDto.setCategoryFirstId(optionalProduct.get().getCategoryFirst().getId());
        productDto.setCategorySecondId(optionalProduct.get().getCategorySecond().getId());
        productDto.setCategoryThirdId(optionalProduct.get().getCategoryThird().getId());

        return productDto;
    }
}