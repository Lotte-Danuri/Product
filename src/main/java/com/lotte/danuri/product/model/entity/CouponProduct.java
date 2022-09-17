package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
@Table(name = "coupon_product")
public class CouponProduct extends BaseEntity{

    private LocalDateTime deletedDate;

    @ManyToOne
    @JoinColumn(name = "Coupon_id")
    @JsonBackReference
    private Coupon coupon;

    @ManyToOne
    @JoinColumn(name = "Product_id")
    @JsonManagedReference
    private Product product;

    public void updateDeleteDate(LocalDateTime now) {
        deletedDate = now;
    }
}
