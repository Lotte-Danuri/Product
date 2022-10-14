package com.lotte.danuri.product.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.lotte.danuri.product.model.entity.CategoryFirst;
import com.lotte.danuri.product.model.entity.CategorySecond;
import com.lotte.danuri.product.model.entity.CategoryThird;
import com.lotte.danuri.product.model.entity.Product;
import lombok.*;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ProductDto {

    private Long id;

    private String productName;
    private String thumbnailUrl;
    private Double price;
    private Long stock;
    private Long storeId;
    private Long likeCount;
    private String productCode;
    private Long warranty;

    private Long categoryFirstId;
    private Long categorySecondId;
    private Long categoryThirdId;

    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    private String createdBy;
    private String updatedBy;

    public ProductDto(Product product) {
        this.id = product.getId();
        this.productName = product.getProductName();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.storeId = product.getStoreId();
        this.likeCount = product.getLikeCount();
        this.productCode = product.getProductCode();
        this.warranty = product.getWarranty();
        this.categoryFirstId = product.getCategoryFirst().getId();
        this.categorySecondId = product.getCategorySecond().getId();
        this.categoryThirdId = product.getCategoryThird().getId();
        this.createdDate = product.getCreatedDate();
        this.updatedDate = product.getUpdatedDate();
    }
}