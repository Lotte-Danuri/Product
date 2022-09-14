package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category_first")
public class CategoryFirst extends BaseEntity{

    private String categoryName;

    @JsonBackReference
    @OneToMany(mappedBy = "categoryFirst",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CategorySecond> categorySeconds;

    @JsonBackReference
    @OneToMany(mappedBy = "categoryFirst",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CategoryThird> categoryThirds;

    @JsonBackReference
    @OneToMany(mappedBy = "categoryFirst",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;

    private int status;
}
