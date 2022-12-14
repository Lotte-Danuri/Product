package com.lotte.danuri.product.service.seller;

import com.lotte.danuri.product.client.OrderServiceClient;
import com.lotte.danuri.product.client.RecommendServiceClient;
import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.CategoryWasDeletedException;
import com.lotte.danuri.product.exception.CategoryNotFoundException;
import com.lotte.danuri.product.exception.ProductNotFoundException;
import com.lotte.danuri.product.exception.ProductWasDeletedException;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.dto.request.CategoryDto;
import com.lotte.danuri.product.model.dto.request.ProductChanceDto;
import com.lotte.danuri.product.model.dto.request.ProductListDto;
import com.lotte.danuri.product.model.dto.response.SellerProductResponseDto;
import com.lotte.danuri.product.model.entity.*;
import com.lotte.danuri.product.repository.*;
import com.lotte.danuri.product.util.S3Upload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SellerProductServiceImpl implements SellerProductService {

    private final ProductRepository productRepository;
    private final CategoryFirstRepository categoryFirstRepository;
    private final CategorySecondRepository categorySecondRepository;
    private final CategoryThirdRepository categoryThirdRepository;
    private final ImageRepository imageRepository;
    private final S3Upload s3Upload;
    private final OrderServiceClient orderServiceClient;
    private final RecommendServiceClient recommendServiceClient;
    private final CircuitBreakerFactory circuitBreakerFactory;
    @Override
    public void createProduct(ProductDto productDto, List<MultipartFile> multipartFileList) {
        Optional<CategoryFirst> categoryFirst = categoryFirstRepository.findById(productDto.getCategoryFirstId());
        Optional<CategorySecond> categorySecond = categorySecondRepository.findById(productDto.getCategorySecondId());
        Optional<CategoryThird> categoryThird = categoryThirdRepository.findById(productDto.getCategoryThirdId());

        // ?????? ??????
        // 1. ??????????????? DB??? ???????????? ?????? ??????
        if (categoryFirst.isEmpty() || categorySecond.isEmpty() || categoryThird.isEmpty()){
            throw new CategoryNotFoundException("Category not present in the database", ErrorCode.CATEGORY_NOT_FOUND);
        }

        // 2. ??????????????? ????????? ??????
        if (categoryFirst.get().getDeletedDate() != null ||
                categorySecond.get().getDeletedDate() != null ||
                categoryThird.get().getDeletedDate() != null){
            throw new CategoryWasDeletedException("Category was deleted in the database", ErrorCode.CATEGORY_WAS_DELETED);
        }

        Product product = productRepository.save(
                Product.builder()
                        .categoryFirst(categoryFirst.get())
                        .categorySecond(categorySecond.get())
                        .categoryThird(categoryThird.get())
                        .productName(productDto.getProductName())
                        .thumbnailUrl(uploadImage(multipartFileList.get(0)))
                        .price(productDto.getPrice())
                        .stock(productDto.getStock())
                        .storeId(productDto.getStoreId())
                        .likeCount(0L)
                        .productCode(productDto.getProductCode())
                        .warranty(productDto.getWarranty())
                        .build()
        );

        // ?????? ?????? ????????? INSERT
        List<Image> imageList = new ArrayList<>();
        multipartFileList.stream().skip(1).forEach(v -> {
            Image image = Image.builder()
                    .imageUrl(uploadImage(v))
                    .product(product)
                    .build();
            imageList.add(image);
        });
        imageRepository.saveAll(imageList);
    }

    @Override
    public List<ProductDto> getProducts(){
        List<Product> products = productRepository.findAllByDeletedDateIsNull();
        List<ProductDto> result = new ArrayList<>();

        products.forEach(v -> {
            ProductDto productDto = new ProductDto(v);
            result.add(productDto);
        });
        return result;
    }

    @Override
    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);

        // ?????? ??????
        // 1. ????????? DB??? ???????????? ?????? ??????
        if(product.isEmpty()){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 2. ????????? ????????? ??????
        if(product.get().getDeletedDate() != null){
            throw new ProductWasDeletedException("Product was deleted in the database", ErrorCode.PRODUCT_WAS_DELETED);
        }

        // ?????? DELETE
        product.get().updateDeletedDate(LocalDateTime.now());
        productRepository.save(product.get());

        // ?????? ?????? ????????? DELETE
        product.get().getImages().forEach(v -> {
            v.updateDeletedDate(LocalDateTime.now());
        });
        imageRepository.saveAll(product.get().getImages());
    }

    @Override
    public void updateProduct(ProductDto productDto, List<MultipartFile> multipartFileList) {
        Optional<Product> product = productRepository.findById(productDto.getId());
        Optional<CategoryFirst> categoryFirst = categoryFirstRepository.findById(productDto.getCategoryFirstId());
        Optional<CategorySecond> categorySecond = categorySecondRepository.findById(productDto.getCategorySecondId());
        Optional<CategoryThird> categoryThird = categoryThirdRepository.findById(productDto.getCategoryThirdId());

        // ?????? ??????
        // 1. ????????? DB??? ???????????? ?????? ??????
        if(product.isEmpty()){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 2. ??????????????? DB??? ???????????? ?????? ??????
        if (categoryFirst.isEmpty() || categorySecond.isEmpty() || categoryThird.isEmpty()){
            throw new CategoryNotFoundException("Category not present in the database", ErrorCode.CATEGORY_NOT_FOUND);
        }

        // 3. ????????? ????????? ??????
        if(product.get().getDeletedDate() != null){
            throw new ProductWasDeletedException("Product was deleted in the database", ErrorCode.PRODUCT_WAS_DELETED);
        }

        // 4. ??????????????? ????????? ??????
        if (categoryFirst.get().getDeletedDate() != null ||
                categorySecond.get().getDeletedDate() != null ||
                categoryThird.get().getDeletedDate() != null){
            throw new CategoryWasDeletedException("Category was deleted in the database", ErrorCode.CATEGORY_WAS_DELETED);
        }

        // ?????? UPDATE
        product.get().update(productDto, categoryFirst.get(), categorySecond.get(), categoryThird.get(), uploadImage(multipartFileList.get(0)));
        productRepository.save(product.get());

        // ?????? ?????? ?????????(??????) DELETE
        List<Image> imageList = new ArrayList<>();
        product.get().getImages().forEach(v -> {
            v.updateDeletedDate(LocalDateTime.now());
            imageList.add(v);
        });
        imageRepository.saveAll(imageList);

        // ?????? ?????? ?????????(??????) INSERT
        List<Image> ListImage = new ArrayList<>();
        multipartFileList.stream().skip(1).forEach(v -> {
            Image image = Image.builder()
                    .imageUrl(uploadImage(v))
                    .product(product.get())
                    .build();
            ListImage.add(image);
        });
        imageRepository.saveAll(ListImage);
    }

    @Override
    public String uploadImage(MultipartFile multipartFile){
        try {
            return s3Upload.upload(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SellerProductResponseDto> getProductsByStoreId(Long storeId){
        List<SellerProductResponseDto> sellerProductResponseDtos = new ArrayList<>();

        List<Product> products = productRepository.findAllByDeletedDateIsNullAndStoreId(storeId);
        ProductListDto productListDto = ProductListDto.builder().
                productId(products.stream()
                                  .map(product -> product.getId())
                                  .toList())
                .build();

        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");

        log.info("Before Call [getClickCount] Method IN [Product-Service]");
        List<Long> productClickCounts = circuitBreaker.run(() -> recommendServiceClient.getClickCountByDate(productListDto),
                throwable -> new ArrayList<>());
        log.info("After Call [getClickCount] Method IN [Product-Service]");

        log.info("Before Call [getOrdersCount] Method IN [Product-Service]");
        List<Long> productOrderCounts = circuitBreaker.run(() -> orderServiceClient.getOrdersCountByDate(productListDto),
                throwable -> new ArrayList<>());
        log.info("After Call [getOrdersCount] Method IN [Product-Service]");

        for(int i=0; i<products.size(); i++){
            sellerProductResponseDtos.add(new SellerProductResponseDto(
                    products.get(i),
                    productClickCounts.get(i),
                    productOrderCounts.get(i),
                    productOrderCounts.get(i)==0?0:productOrderCounts.get(i).doubleValue()/productClickCounts.get(i).doubleValue()*100
                )
            );
        }
        return sellerProductResponseDtos;
    }

    @Override
    public List<SellerProductResponseDto> getProductsByCategory(CategoryDto categoryDto){

        if (categoryDto.getCategoryFirstId() != null) {
            Optional<CategoryFirst> categoryFirst = categoryFirstRepository.findById(categoryDto.getCategoryFirstId());
            if (categoryFirst.isEmpty()) {
                throw new CategoryNotFoundException("Category not present in the database", ErrorCode.CATEGORY_NOT_FOUND);
            }
            if (categoryFirst.get().getDeletedDate() != null) {
                throw new CategoryWasDeletedException("Category was deleted in the database", ErrorCode.CATEGORY_WAS_DELETED);
            }
        }

        if (categoryDto.getCategorySecondId() != null) {
            Optional<CategorySecond> categorySecond = categorySecondRepository.findById(categoryDto.getCategorySecondId());
            if (categorySecond.isEmpty()) {
                throw new CategoryNotFoundException("Category not present in the database", ErrorCode.CATEGORY_NOT_FOUND);
            }
            if (categorySecond.get().getDeletedDate() != null) {
                throw new CategoryWasDeletedException("Category was deleted in the database", ErrorCode.CATEGORY_WAS_DELETED);
            }
        }

        if (categoryDto.getCategoryThirdId() != null) {
            Optional<CategoryThird> categoryThird = categoryThirdRepository.findById(categoryDto.getCategoryThirdId());
            if (categoryThird.isEmpty()) {
                throw new CategoryNotFoundException("Category not present in the database", ErrorCode.CATEGORY_NOT_FOUND);
            }
            if (categoryThird.get().getDeletedDate() != null) {
                throw new CategoryWasDeletedException("Category was deleted in the database", ErrorCode.CATEGORY_WAS_DELETED);
            }
        }

        List<Product> productList = new ArrayList<>();

        if (categoryDto.getCategoryFirstId() == null) {
            if (categoryDto.getProductName() == null) {
                productList = productRepository.findAllByStoreIdAndDeletedDateIsNull(
                        categoryDto.getStordId()
                );
            } else {
                productList = productRepository.findAllByStoreIdAndProductNameContainingAndDeletedDateIsNull(
                        categoryDto.getStordId(),
                        categoryDto.getProductName()
                );
            }
        }
        else if (categoryDto.getCategorySecondId() == null) {
            if (categoryDto.getProductName() == null) {
                productList = productRepository.findAllByStoreIdAndCategoryFirstIdAndDeletedDateIsNull(
                        categoryDto.getStordId(),
                        categoryDto.getCategoryFirstId()
                );
            } else {
                productList = productRepository.findAllByStoreIdAndProductNameContainingAndCategoryFirstIdAndDeletedDateIsNull(
                        categoryDto.getStordId(),
                        categoryDto.getProductName(),
                        categoryDto.getCategoryFirstId()
                );
            }
        }
        else if (categoryDto.getCategoryThirdId() == null) {
            if (categoryDto.getProductName() == null) {
                productList = productRepository.findAllByStoreIdAndCategorySecondIdAndDeletedDateIsNull(
                        categoryDto.getStordId(),
                        categoryDto.getCategorySecondId()
                );
            } else {
                productList = productRepository.findAllByStoreIdAndProductNameContainingAndCategorySecondIdAndDeletedDateIsNull(
                        categoryDto.getStordId(),
                        categoryDto.getProductName(),
                        categoryDto.getCategorySecondId()
                );
            }
        }
        else{
            if (categoryDto.getProductName() == null) {
                productList = productRepository.findAllByStoreIdAndCategoryThirdIdAndDeletedDateIsNull(
                        categoryDto.getStordId(),
                        categoryDto.getCategoryThirdId()
                );
            } else {
                productList = productRepository.findAllByStoreIdAndProductNameContainingAndCategoryThirdIdAndDeletedDateIsNull(
                        categoryDto.getStordId(),
                        categoryDto.getProductName(),
                        categoryDto.getCategoryThirdId()
                );
            }
        }

        List<SellerProductResponseDto> sellerProductResponseDtoList = new ArrayList<>();

        productList.forEach(v -> {
            sellerProductResponseDtoList.add(new SellerProductResponseDto(v));
        });

        return sellerProductResponseDtoList;
    }

    @Override
    public List<SellerProductResponseDto> getProductChance(ProductChanceDto productChanceDto){
        List<SellerProductResponseDto> sellerProductResponseDtos = new ArrayList<>();

        List<Product> products = productRepository.findAllByDeletedDateIsNullAndStoreId(productChanceDto.getStoreId());
        ProductListDto productListDto = ProductListDto.builder()
                        .productId(products.stream()
                            .map(product -> product.getId())
                            .toList())
                        .startDate(productChanceDto.getStartDate())
                        .endDate(productChanceDto.getEndDate())
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

        for(int i=0; i<products.size(); i++){
            sellerProductResponseDtos.add(new SellerProductResponseDto(
                            products.get(i),
                            productClickCounts.get(i),
                            productOrderCounts.get(i),
                            productOrderCounts.get(i)==0?0:productOrderCounts.get(i).doubleValue()/productClickCounts.get(i).doubleValue()*100
                    )
            );
        }

        List<SellerProductResponseDto> result = sellerProductResponseDtos
                                    .stream()
                                    .sorted(Comparator.comparingDouble(SellerProductResponseDto::getConversionRate))
                                    .filter(v -> v.getOrderCount() <= 5)
                                    .collect(Collectors.toList());
        return result;
    }
}