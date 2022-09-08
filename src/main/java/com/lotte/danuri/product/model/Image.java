package com.lotte.danuri.product.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "image")
public class Image extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "Product_id")
    @JsonManagedReference
    private Product product;

    private String imageUrl;
}
