package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lotte.danuri.product.model.dto.ProductDto;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
    private String productCode;
    private Long warranty;
    private LocalDateTime deletedDate;

    @ManyToOne
    @JoinColumn(name = "CategoryFirst_id")
    @JsonBackReference
    private CategoryFirst categoryFirst;

    @ManyToOne
    @JoinColumn(name = "CategorySecond_id")
    @JsonBackReference
    private CategorySecond categorySecond;

    @ManyToOne
    @JoinColumn(name = "CategoryThird_id")
    @JsonBackReference
    private CategoryThird categoryThird;

    @JsonManagedReference
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;

    @JsonManagedReference
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CouponProduct> couponProducts;

    public void update(ProductDto productDto, CategoryFirst categoryFirst, CategorySecond categorySecond, CategoryThird categoryThird, String multipartFile){
        this.productName = productDto.getProductName();
        this.thumbnailUrl = productDto.getThumbnailUrl();
        this.price = productDto.getPrice();
        this.stock = productDto.getStock();
        this.storeId = productDto.getStoreId();
        this.productCode = productDto.getProductCode();
        this.warranty = productDto.getWarranty();
        this.categoryFirst = categoryFirst;
        this.categorySecond = categorySecond;
        this.categoryThird = categoryThird;
        this.thumbnailUrl = multipartFile;
    }

    public void updateDeletedDate(LocalDateTime now) {
        this.deletedDate = now;
    }
}
