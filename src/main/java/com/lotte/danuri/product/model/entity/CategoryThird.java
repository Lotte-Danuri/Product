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
@Table(name = "category_third")
public class CategoryThird extends BaseEntity{

    private String categoryName;
    private LocalDateTime deletedDate;

    @ManyToOne
    @JoinColumn(name = "CategorySecond_id")
    @JsonBackReference
    private CategorySecond categorySecond;

    @ManyToOne
    @JoinColumn(name = "CategoryFirst_id")
    @JsonBackReference
    private CategoryFirst categoryFirst;
}
