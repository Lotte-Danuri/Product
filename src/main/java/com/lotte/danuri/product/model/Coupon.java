package com.lotte.danuri.product.model;

import com.fasterxml.jackson.databind.DatabindException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;
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
