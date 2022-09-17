package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lotte.danuri.product.model.dto.ProductDto;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "product")
public class Product extends BaseEntity{

    private String productName;
    private String thumbnailUrl;
    private Double price;
    private Long stock;
    private Long storeId;
    private Long likeCount;
    private LocalDateTime deletedDate;

    @ManyToOne
    @JoinColumn(name = "CategoryFirst_id")
    @JsonManagedReference
    private CategoryFirst categoryFirst;

    @ManyToOne
    @JoinColumn(name = "CategorySecond_id")
    @JsonManagedReference
    private CategorySecond categorySecond;

    @ManyToOne
    @JoinColumn(name = "CategoryThird_id")
    @JsonManagedReference
    private CategoryThird categoryThird;

    @JsonManagedReference
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;

    @JsonBackReference
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CouponProduct> couponProducts;

    public void update(ProductDto productDto, CategoryFirst categoryFirst, CategorySecond categorySecond, CategoryThird categoryThird){
        this.productName = productDto.getProductName();
        this.thumbnailUrl = productDto.getThumbnailUrl();
        this.price = productDto.getPrice();
        this.stock = productDto.getStock();
        this.storeId = productDto.getStoreId();
        this.categoryFirst = categoryFirst;
        this.categorySecond = categorySecond;
        this.categoryThird = categoryThird;
    }

    public void updateDeletedDate(LocalDateTime now) {
        this.deletedDate = now;
    }
}
