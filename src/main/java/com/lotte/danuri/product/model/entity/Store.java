package com.lotte.danuri.product.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "store")
public class Store extends BaseEntity{

    private Long memberId;

    private String description;

    private String address;

    private Double score;

    private String ownerName;

    private String ownerNumber;

    private String imageUrl;

    private int status;
}
