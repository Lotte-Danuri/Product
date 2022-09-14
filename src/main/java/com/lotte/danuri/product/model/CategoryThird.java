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
@Table(name = "category_third")
public class CategoryThird extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "CategorySecond_id")
    @JsonManagedReference
    private CategorySecond categorySecond;

    @ManyToOne
    @JoinColumn(name = "CategoryFirst_id")
    @JsonManagedReference
    private CategoryFirst categoryFirst;

    private String categoryName;

    @JsonBackReference
    @OneToMany(mappedBy = "categoryThird",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;

    private int status;
}
