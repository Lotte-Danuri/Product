package com.lotte.danuri.product.service.buyer;

import com.lotte.danuri.product.client.MemberServiceClient;
import com.lotte.danuri.product.client.OrderServiceClient;
import com.lotte.danuri.product.client.RecommendServiceClient;
import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.ProductNotFoundException;
import com.lotte.danuri.product.exception.ProductWasDeletedException;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.dto.request.BrandCategoryDto;
import com.lotte.danuri.product.model.dto.request.ProductByConditionDto;
import com.lotte.danuri.product.model.dto.request.ProductListByCodeDto;
import com.lotte.danuri.product.model.dto.request.ProductListDto;
import com.lotte.danuri.product.model.dto.response.ProductDetailResponseDto;
import com.lotte.danuri.product.model.dto.response.SellerProductResponseDto;
import com.lotte.danuri.product.model.dto.response.StoreInfoRespDto;
import com.lotte.danuri.product.model.dto.response.StoreRespDto;
import com.lotte.danuri.product.model.entity.Product;
import com.lotte.danuri.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.lotte.danuri.product.util.DeDuplication.deduplication;

@Service
@RequiredArgsConstructor
@Slf4j
public class BuyerProductServiceImpl implements BuyerProductService{

    private final ProductRepository productRepository;
    private final MemberServiceClient memberServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    private final RecommendServiceClient recommendServiceClient;
    private final OrderServiceClient orderServiceClient;
    @Override
    public List<ProductDto> getProducts(){
        log.info("Before Retrieve [getProducts] Method IN [Product-Service]");
        List<Product> products = productRepository.findAllByDeletedDateIsNull();
        List<ProductDto> ProductDtoList = new ArrayList<>();
        products.forEach(v -> {
            ProductDto productDto = new ProductDto(v);
            ProductDtoList.add(productDto);
        });

        List<ProductDto> result = deduplication(ProductDtoList, ProductDto::getProductCode);
        result.forEach(v -> {
            StoreInfoRespDto storeInfoRespDto = memberServiceClient.getNames(v.getStoreId());
            v.updateBrandName(storeInfoRespDto.getBrandName());
        });
        log.info("After Retrieve [getProducts] Method IN [Product-Service]");
        return result;
    }

    @Override
    public ProductDetailResponseDto getProduct(Long productId){
        log.info("Before Retrieve [getProduct] Method IN [Product-Service]");
        Optional<Product> product = productRepository.findById(productId);

        // 예외 처리
        // 1. 상품이 DB에 존재하지 않을 경우
        if(product.isEmpty()){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 2. 상품이 삭제된 경우
        if(product.get().getDeletedDate() != null){
            throw new ProductWasDeletedException("Product was deleted in the database", ErrorCode.PRODUCT_WAS_DELETED);
        }

        List<String> imageList = new ArrayList<>();
        product.get().getImages().forEach(v -> {
            imageList.add(v.getImageUrl());
        });
        log.info("Before Call [getNames] Method IN [Product-Service]");
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
        StoreInfoRespDto storeInfoRespDto = circuitBreaker.run(() -> memberServiceClient.getNames(product.get().getStoreId()),
                throwable -> new StoreInfoRespDto());
        log.info("After Call [getNames] Method IN [Product-Service]");
        ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(product.get(),imageList, storeInfoRespDto);
        log.info("After Retrieve [getProduct] Method IN [Product-Service]");
        return productDetailResponseDto;
    }

    @Override
    public List<ProductDto> getProductsByCondition(ProductByConditionDto productByConditionDto) {
        List<Long> brandId = productByConditionDto.getBrandId();
        List<Long> storeId = new ArrayList<>();

        brandId.forEach(v -> {
            log.info("Before Call [getStoreId] Method IN [Product-Service]");
            CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
            List<Long> storeIdList = circuitBreaker.run(() -> memberServiceClient.getStoreId(v),
                    throwable -> new ArrayList<>());
            log.info("Before After [getStoreId] Method IN [Product-Service]");
            storeIdList.forEach(w -> {
                storeId.add(w);
            });
        });

        List<Product> productList = productRepository.findAllByPriceBetweenAndCategoryThirdIdInAndStoreIdIn(
                productByConditionDto.getMinPrice(),
                productByConditionDto.getMaxPrice(),
                productByConditionDto.getCategoryThirdId(),
                storeId);

        List<ProductDto> productDtoList = new ArrayList<>();

        productList.forEach(v -> {
            ProductDto productDto = new ProductDto(v);
            productDtoList.add(productDto);
        });

        List<ProductDto> result = deduplication(productDtoList, ProductDto::getProductCode);
        return result;
    }

    @Override
    public List<ProductDto> getProductListByCode(ProductListByCodeDto productListByCodeDto){
        List<Product> productList = new ArrayList<>();
        productListByCodeDto.getProductCode().forEach(v -> {
            List<Product> products = productRepository.findAllByProductCodeAndDeletedDateIsNull(v);
            productList.add(products.get(0));
        });

        List<ProductDto> result = new ArrayList<>();

        productList.forEach(v -> {
            ProductDto productDto = new ProductDto(v);
            result.add(productDto);
        });

        return result;
    }

    @Override
    public List<ProductDetailResponseDto> getProductListByProductCode(String productCode){
        List<ProductDetailResponseDto> result = new ArrayList<>();

        List<Product> productList = productRepository.findAllByProductCode(productCode);
        productList.forEach(v -> {
            List<String> imageList = new ArrayList<>();
            v.getImages().forEach(w -> {
                imageList.add(w.getImageUrl());
            });

            log.info("Before Call [getNames] Method IN [Product-Service]");
            CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
            StoreInfoRespDto storeInfoRespDto = circuitBreaker.run(() -> memberServiceClient.getNames(v.getStoreId()),
                    throwable -> new StoreInfoRespDto());
            log.info("After Call [getNames] Method IN [Product-Service]");
            ProductDetailResponseDto productDetailResponseDto = new ProductDetailResponseDto(v,imageList, storeInfoRespDto);
            result.add(productDetailResponseDto);
        });

        return result;
    }

    @Override
    public List<ProductDto> getProductListById(ProductListDto productListDto){
        List<Product> productList = new ArrayList<>();
        productListDto.getProductId().forEach(v -> {
            productList.add(productRepository.findById(v).get());
        });

        List<ProductDto> result = new ArrayList<>();
        productList.forEach(v -> {
            ProductDto productDto = new ProductDto(v);
            result.add(productDto);
        });
        return result;
    }

    @Override
    public List<ProductDto> getProductListByBrand(BrandCategoryDto brandCategoryDto){
        List<ProductDto> result = new ArrayList<>();
        List<Product> productList = new ArrayList<>();
        if (brandCategoryDto.getStoreId() == null) {
            log.info("Before Call [getProductListByBrand] Method IN [Product-Service]");
            CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
            List<StoreRespDto> storeRespDtoList = circuitBreaker.run(() -> memberServiceClient.getStore(brandCategoryDto.getBrandId()),
                    throwable -> new ArrayList<>());
            storeRespDtoList.forEach(v -> {
                System.out.println(v.getStoreId());
            });
            log.info("After Call [getProductListByBrand] Method IN [Product-Service]");

            if (brandCategoryDto.getCategoryFirstId() != null ) {
                productList = productRepository.findAllByStoreIdInAndCategoryFirstIdAndDeletedDateIsNull(
                        storeRespDtoList.stream()
                                .map(storeRespDto -> storeRespDto.getStoreId())
                                .toList(),
                        brandCategoryDto.getCategoryFirstId()
                );
            } else if (brandCategoryDto.getCategorySecondId() != null) {
                productList = productRepository.findAllByStoreIdInAndCategorySecondIdAndDeletedDateIsNull(
                        storeRespDtoList.stream()
                                .map(storeRespDto -> storeRespDto.getStoreId())
                                .toList(),
                        brandCategoryDto.getCategorySecondId()
                );

            } else if (brandCategoryDto.getCategoryThirdId() != null) {
                productList = productRepository.findAllByStoreIdInAndCategoryThirdIdAndDeletedDateIsNull(
                        storeRespDtoList.stream()
                                .map(storeRespDto -> storeRespDto.getStoreId())
                                .toList(),
                        brandCategoryDto.getCategoryThirdId()
                );
            } else {
                productList = productRepository.findAllByStoreIdInAndDeletedDateIsNull(
                        storeRespDtoList.stream()
                                .map(storeRespDto -> storeRespDto.getStoreId())
                                .toList());
            }

            List<ProductDto> productDtoList = new ArrayList<>();
            productList.forEach(v -> {
                productDtoList.add(new ProductDto(v));
            });

            result = deduplication(productDtoList, ProductDto::getProductCode);
        }
        else {
            if (brandCategoryDto.getCategoryFirstId() != null ) {
                productList = productRepository.findAllByStoreIdAndCategoryFirstIdAndDeletedDateIsNull(
                        brandCategoryDto.getStoreId(),
                        brandCategoryDto.getCategoryFirstId()
                );
            } else if (brandCategoryDto.getCategorySecondId() != null) {
                productList = productRepository.findAllByStoreIdAndCategorySecondIdAndDeletedDateIsNull(
                        brandCategoryDto.getStoreId(),
                        brandCategoryDto.getCategorySecondId()
                );

            } else if (brandCategoryDto.getCategoryThirdId() != null) {
                productList = productRepository.findAllByStoreIdAndCategoryThirdIdAndDeletedDateIsNull(
                        brandCategoryDto.getStoreId(),
                        brandCategoryDto.getCategoryThirdId()
                );
            } else {
                productList = productRepository.findAllByStoreIdAndDeletedDateIsNull(
                        brandCategoryDto.getStoreId()
                );
            }

            List<ProductDto> productDtoList = new ArrayList<>();
            productList.forEach(v -> {
                productDtoList.add(new ProductDto(v));
            });

            result = productDtoList;
        }

        return result;
    }

    @Override
    public List<SellerProductResponseDto> getBestProductList(){
        List<SellerProductResponseDto> sellerProductResponseDtos = new ArrayList<>();
        List<ProductDto> productDtoList = new ArrayList<>();
        List<ProductDto> productDtos = new ArrayList<>();
        List<Product> products = productRepository.findAllByDeletedDateIsNull();
        products.forEach(v -> {
            productDtoList.add(new ProductDto(v));
        });

        //중복제거 된 product list
        productDtos = deduplication(productDtoList, ProductDto::getProductCode);

        ProductListDto productListDto = ProductListDto.builder()
                .productId(productDtos.stream()
                        .map(product -> product.getId())
                        .toList())
                .startDate(LocalDateTime.now().minusDays(7))
                .endDate(LocalDateTime.now())
                .build();
        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");

        log.info("Before Call [getClickCountByDate] Method IN [Product-Service]");
        List<Long> productClickCounts = circuitBreaker.run(() -> recommendServiceClient.getClickCountByDate(productListDto),
                throwable -> new ArrayList<>());
        log.info("After Call [getClickCountByDate] Method IN [Product-Service]");

        log.info("Before Call [getOrdersCountByDate] Method IN [Product-Service]");
        List<Long> productOrderCounts = circuitBreaker.run(() -> orderServiceClient.getOrdersCountByDate(productListDto),
                throwable -> new ArrayList<>());
        log.info("After Call [getOrdersCountByDate] Method IN [Product-Service]");

        for(int i=0; i<productDtos.size(); i++){
            sellerProductResponseDtos.add(new SellerProductResponseDto(
                            productDtos.get(i),
                            productClickCounts.get(i),
                            productOrderCounts.get(i),
                            productOrderCounts.get(i)==0?0:productOrderCounts.get(i).doubleValue()/productClickCounts.get(i).doubleValue()*100
                    )
            );
        }

        List<SellerProductResponseDto> clickRankInHalf = sellerProductResponseDtos
                        .stream()
                        .sorted(Comparator.comparingDouble(SellerProductResponseDto::getClickCount).reversed())
                        .limit(50)
                        .collect(Collectors.toList());

        List<SellerProductResponseDto> orderRankInHalf = sellerProductResponseDtos
                        .stream()
                        .sorted(Comparator.comparingDouble(SellerProductResponseDto::getOrderCount).reversed())
                        .limit(50)
                        .collect(Collectors.toList());

        List<SellerProductResponseDto> result = new ArrayList<>();

        result = clickRankInHalf.stream()
                        .filter(sellerProductResponseDto  -> orderRankInHalf.stream().anyMatch(Predicate.isEqual(sellerProductResponseDto)))
                        .sorted(Comparator.comparingDouble(SellerProductResponseDto::getConversionRate).reversed())
                        .collect(Collectors.toList());
        return result;
    }
}