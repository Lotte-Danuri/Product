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
    private String storeName;
    private Long likeCount;
    private String productCode;
    private Long warranty;

    private String categoryFirstName;
    private String categorySecondName;
    private String categoryThirdName;

    private List<String> imageList;
    public ProductDetailResponseDto(Product product, List<String> imageList) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.price = product.getPrice();
        this.stock = product.getStock();
        //TODO : storeId를 이용해 member 서비스에서 storeName 가져오기
        this.storeName = "임시";
        this.likeCount = product.getLikeCount();
        this.productCode = product.getProductCode();
        this.warranty = product.getWarranty();
        this.categoryFirstName = product.getCategoryFirst().getCategoryName();
        this.categorySecondName = product.getCategorySecond().getCategoryName();
        this.categoryThirdName = product.getCategoryThird().getCategoryName();
        this.imageList = imageList;

    }
}
