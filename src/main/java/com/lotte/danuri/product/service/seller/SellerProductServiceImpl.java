package com.lotte.danuri.product.service.seller;

import com.lotte.danuri.product.error.ErrorCode;
import com.lotte.danuri.product.exception.CategoryWasDeletedException;
import com.lotte.danuri.product.exception.CategoryNotFoundException;
import com.lotte.danuri.product.exception.ProductNotFoundException;
import com.lotte.danuri.product.exception.ProductWasDeletedException;
import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.*;
import com.lotte.danuri.product.repository.*;
import com.lotte.danuri.product.util.S3Upload;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerProductServiceImpl implements SellerProductService {

    private final ProductRepository productRepository;
    private final CategoryFirstRepository categoryFirstRepository;
    private final CategorySecondRepository categorySecondRepository;
    private final CategoryThirdRepository categoryThirdRepository;
    private final ImageRepository imageRepository;
    private final S3Upload s3Upload;

    public void createProduct(ProductDto productDto, List<MultipartFile> multipartFileList) {
        Optional<CategoryFirst> categoryFirst = categoryFirstRepository.findById(productDto.getCategoryFirstId());
        Optional<CategorySecond> categorySecond = categorySecondRepository.findById(productDto.getCategorySecondId());
        Optional<CategoryThird> categoryThird = categoryThirdRepository.findById(productDto.getCategoryThirdId());

        // 예외 처리
        // 1. 카테고리가 DB에 존재하지 않을 경우
        if (categoryFirst.isEmpty() || categorySecond.isEmpty() || categoryThird.isEmpty()){
            throw new CategoryNotFoundException("Category not present in the database", ErrorCode.CATEGORY_NOT_FOUND);
        }

        // 2. 카테고리가 삭제된 경우
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

        // 상품 상세 이미지 INSERT
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

    public List<ProductDto> getProducts(){
        List<Product> products = productRepository.findAllByDeletedDateIsNull();
        List<ProductDto> result = new ArrayList<>();

        products.forEach(v -> {
            ProductDto productDto = new ProductDto(v);
            result.add(productDto);
        });
        return result;
    }

    public void deleteProduct(Long id) {
        Optional<Product> product = productRepository.findById(id);

        // 예외 처리
        // 1. 상품이 DB에 존재하지 않을 경우
        if(product.isEmpty()){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 2. 상품이 삭제된 경우
        if(product.get().getDeletedDate() != null){
            throw new ProductWasDeletedException("Product was deleted in the database", ErrorCode.PRODUCT_WAS_DELETED);
        }

        // 상품 DELETE
        product.get().updateDeletedDate(LocalDateTime.now());
        productRepository.save(product.get());

        // 상품 상세 이미지 DELETE
        product.get().getImages().forEach(v -> {
            v.updateDeletedDate(LocalDateTime.now());
        });
        imageRepository.saveAll(product.get().getImages());
    }

    public void updateProduct(ProductDto productDto, List<MultipartFile> multipartFileList) {
        Optional<Product> product = productRepository.findById(productDto.getId());
        Optional<CategoryFirst> categoryFirst = categoryFirstRepository.findById(productDto.getCategoryFirstId());
        Optional<CategorySecond> categorySecond = categorySecondRepository.findById(productDto.getCategorySecondId());
        Optional<CategoryThird> categoryThird = categoryThirdRepository.findById(productDto.getCategoryThirdId());

        // 예외 처리
        // 1. 상품이 DB에 존재하지 않을 경우
        if(product.isEmpty()){
            throw new ProductNotFoundException("Product not present in the database", ErrorCode.PRODUCT_NOT_FOUND);
        }

        // 2. 카테고리가 DB에 존재하지 않을 경우
        if (categoryFirst.isEmpty() || categorySecond.isEmpty() || categoryThird.isEmpty()){
            throw new CategoryNotFoundException("Category not present in the database", ErrorCode.CATEGORY_NOT_FOUND);
        }

        // 3. 상품이 삭제된 경우
        if(product.get().getDeletedDate() != null){
            throw new ProductWasDeletedException("Product was deleted in the database", ErrorCode.PRODUCT_WAS_DELETED);
        }

        // 4. 카테고리가 삭제된 경우
        if (categoryFirst.get().getDeletedDate() != null ||
                categorySecond.get().getDeletedDate() != null ||
                categoryThird.get().getDeletedDate() != null){
            throw new CategoryWasDeletedException("Category was deleted in the database", ErrorCode.CATEGORY_WAS_DELETED);
        }

        // 상품 UPDATE
        product.get().update(productDto, categoryFirst.get(), categorySecond.get(), categoryThird.get(), uploadImage(multipartFileList.get(0)));
        productRepository.save(product.get());

        // 상품 상세 이미지(기존) DELETE
        List<Image> imageList = new ArrayList<>();
        product.get().getImages().forEach(v -> {
            v.updateDeletedDate(LocalDateTime.now());
            imageList.add(v);
        });
        imageRepository.saveAll(imageList);

        // 상품 상세 이미지(신규) INSERT
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

    public String uploadImage(MultipartFile multipartFile){
        try {
            return s3Upload.upload(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}