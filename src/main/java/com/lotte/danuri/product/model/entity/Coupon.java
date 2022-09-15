package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "coupon")
public class Coupon extends BaseEntity{

    private String name;

    private String contents;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Double discountRate;

    private LocalDateTime deletedDate;

    private Double minOrderPrice;

    private Double maxDiscountPrice;

    private Long storeId;

    @JsonBackReference
    @OneToMany(mappedBy = "coupon",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CouponProduct> couponProducts;
}
