package com.example.demo.dto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
@Entity(name = "product")
public class Product {
    @Id
    @SequenceGenerator(
        name="product_sequence",
        sequenceName = "product_sequence",
        allocationSize = 1
    )

    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "product_sequence"
    )
    
    private Long id;
    private String name;
    private String brand;
    private double price;
    private boolean promotion;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name= "category_id", referencedColumnName = "id")
    private Category category;
}
