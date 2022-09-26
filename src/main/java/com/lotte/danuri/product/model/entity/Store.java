package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
@Table(name = "store")
public class Store extends BaseEntity{

    private String address;
    private String description;
    private Long memberId;
    private String ownerName;
    private String ownerNumber;
    private Double score;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "Brand_id")
    @JsonBackReference
    private Brand brand;

    private LocalDateTime deletedDate;
}
