package com.lotte.danuri.product.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "category_second")
public class CategorySecond extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "CategoryFirst_id")
    @JsonManagedReference
    private CategoryFirst categoryFirst;

    private String categoryName;

    @JsonBackReference
    @OneToMany(mappedBy = "categorySecond",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CategoryThird> categoryThirds;

    @JsonBackReference
    @OneToMany(mappedBy = "categorySecond",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;

    private int status;
}
