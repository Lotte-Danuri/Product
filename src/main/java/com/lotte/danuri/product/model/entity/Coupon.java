package com.lotte.danuri.product.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    private String appliedTo;

    private int status;
}
