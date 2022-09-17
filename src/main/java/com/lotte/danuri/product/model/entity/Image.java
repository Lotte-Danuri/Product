package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
@Table(name = "image")
public class Image extends BaseEntity{

    private String imageUrl;
    private LocalDateTime deletedDate;

    @ManyToOne
    @JoinColumn(name = "Product_id")
    @JsonBackReference
    private Product product;
}
