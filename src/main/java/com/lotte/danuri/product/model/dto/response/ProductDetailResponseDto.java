package com.lotte.danuri.product.model.dto.response;

import com.lotte.danuri.product.model.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductDetailResponseDto {
    private Long id;

    private String productName;
    private String thumbnailUrl;
    private Double price;
    private Long stock;
    private Long storeId;
    private String storeName;
    private Long likeCount;
    private String productCode;
    private Long warranty;

    private String categoryFirstName;
    private String categorySecondName;
    private String categoryThirdName;

    private List<String> imageList;
    public ProductDetailResponseDto(Product product, List<String> imageList, String storeName) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.storeId = product.getStoreId();
        this.storeName = storeName;
        this.likeCount = product.getLikeCount();
        this.productCode = product.getProductCode();
        this.warranty = product.getWarranty();
        this.categoryFirstName = product.getCategoryFirst().getCategoryName();
        this.categorySecondName = product.getCategorySecond().getCategoryName();
        this.categoryThirdName = product.getCategoryThird().getCategoryName();
        this.imageList = imageList;

    }
}
