package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends BaseEntity{

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

    private String productName;

    private String thumbnailUrl;

    private Double price;

    private Long stock;

    private Long storeId;

    private Long likeCount;

    private LocalDateTime deletedDate;


    @JsonManagedReference
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Image> images;

    @JsonBackReference
    @OneToMany(mappedBy = "product",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CouponProduct> couponProducts;
}
