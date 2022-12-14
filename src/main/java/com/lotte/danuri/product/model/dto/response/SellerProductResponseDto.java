package com.lotte.danuri.product.model.dto.response;

import com.lotte.danuri.product.model.dto.ProductDto;
import com.lotte.danuri.product.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class SellerProductResponseDto {
    private Long id;
    private String productName;
    private String thumbnailUrl;
    private Double price;
    private Long stock;
    private Long storeId;
    private Long likeCount;
    private String productCode;
    private Long warranty;
    private String categoryFirstName;
    private String categorySecondName;
    private String categoryThirdName;

    private Long categoryFirstId;
    private Long categorySecondId;
    private Long categoryThirdId;
    private Long clickCount;
    private Long orderCount;
    private Double conversionRate;


    public SellerProductResponseDto(Product product, Long clickCount, Long orderCount, Double conversionRate) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.storeId = product.getStoreId();
        this.likeCount = product.getLikeCount();
        this.productCode = product.getProductCode();
        this.warranty = product.getWarranty();
        this.categoryFirstName = product.getCategoryFirst().getCategoryName();
        this.categorySecondName = product.getCategorySecond().getCategoryName();
        this.categoryThirdName = product.getCategoryThird().getCategoryName();
        this.clickCount = clickCount;
        this.orderCount = orderCount;
        this.conversionRate = conversionRate;
    }

    public SellerProductResponseDto(ProductDto productDto, Long clickCount, Long orderCount, Double conversionRate) {
        this.id = productDto.getId();
        this.productName = productDto.getProductName();
        this.thumbnailUrl = productDto.getThumbnailUrl();
        this.price = productDto.getPrice();
        this.stock = productDto.getStock();
        this.storeId = productDto.getStoreId();
        this.likeCount = productDto.getLikeCount();
        this.productCode = productDto.getProductCode();
        this.warranty = productDto.getWarranty();
        this.categoryFirstId = productDto.getCategoryFirstId();
        this.categorySecondId = productDto.getCategorySecondId();
        this.categoryThirdId = productDto.getCategoryThirdId();
        this.clickCount = clickCount;
        this.orderCount = orderCount;
        this.conversionRate = conversionRate;
    }

    public SellerProductResponseDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.storeId = product.getStoreId();
        this.likeCount = product.getLikeCount();
        this.productCode = product.getProductCode();
        this.warranty = product.getWarranty();
        this.categoryFirstName = product.getCategoryFirst().getCategoryName();
        this.categorySecondName = product.getCategorySecond().getCategoryName();
        this.categoryThirdName = product.getCategoryThird().getCategoryName();
        this.categoryFirstId = product.getCategoryFirst().getId();
        this.categorySecondId = product.getCategorySecond().getId();
        this.categoryThirdId = product.getCategoryThird().getId();
    }
}
