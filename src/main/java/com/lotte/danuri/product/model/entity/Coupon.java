package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.lotte.danuri.product.model.dto.CouponDto;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
@Table(name = "coupon")
public class Coupon extends BaseEntity{

    private String name;
    private String contents;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startDate;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
    private Double discountRate;
    private Double minOrderPrice;
    private Double maxDiscountPrice;
    private Long storeId;

    private LocalDateTime deletedDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "coupon",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CouponProduct> couponProducts;

    public void updateDeleteDate(LocalDateTime now) {
        deletedDate = now;
    }

    public void update(CouponDto couponDto) {
        this.name = couponDto.getName();
        this.contents = couponDto.getContents();
        this.startDate = couponDto.getStartDate();
        this.endDate = couponDto.getEndDate();
        this.discountRate = couponDto.getDiscountRate();
        this.minOrderPrice = couponDto.getMinOrderPrice();
        this.maxDiscountPrice = couponDto.getMaxDiscountPrice();
        this.storeId = couponDto.getStoreId();
    }
}
