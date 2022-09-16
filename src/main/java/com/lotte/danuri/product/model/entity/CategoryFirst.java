package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category_first")
public class CategoryFirst extends BaseEntity{

    private String categoryName;


    @JsonManagedReference
    @OneToMany(mappedBy = "categoryFirst",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CategorySecond> categorySeconds;

    @JsonManagedReference
    @OneToMany(mappedBy = "categoryFirst",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CategoryThird> categoryThirds;

    @JsonManagedReference
    @OneToMany(mappedBy = "categoryFirst",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;

    private LocalDateTime deletedDate;
}
