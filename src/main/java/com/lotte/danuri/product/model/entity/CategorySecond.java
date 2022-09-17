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
@Table(name = "category_second")
public class CategorySecond extends BaseEntity{

    private String categoryName;
    private LocalDateTime deletedDate;

    @ManyToOne
    @JoinColumn(name = "CategoryFirst_id")
    @JsonManagedReference
    private CategoryFirst categoryFirst;

    @JsonBackReference
    @OneToMany(mappedBy = "categorySecond",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<CategoryThird> categoryThirds;

    @JsonBackReference
    @OneToMany(mappedBy = "categorySecond",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Product> products;
}
