package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
@Table(name = "brand")
public class Brand extends BaseEntity{

    private String brandName;
    private String imageUrl;
    private String description;

    private LocalDateTime deletedDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "brand",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Store> stores;
}
