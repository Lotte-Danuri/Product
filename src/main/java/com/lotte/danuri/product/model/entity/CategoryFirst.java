package com.lotte.danuri.product.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Builder
@Table(name = "category_first")
public class CategoryFirst extends BaseEntity{

    private String categoryName;
    private LocalDateTime deletedDate;

    @JsonManagedReference
    @OneToMany(mappedBy = "categoryFirst",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CategorySecond> categorySeconds;

    @JsonManagedReference
    @OneToMany(mappedBy = "categoryFirst",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CategoryThird> categoryThirds;

    @JsonManagedReference
    @OneToMany(mappedBy = "categoryFirst",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;

    @Builder
    public CategoryFirst(String categoryName, LocalDateTime deletedDate) {
        this.categoryName = categoryName;
        this.deletedDate = deletedDate;
    }
}
