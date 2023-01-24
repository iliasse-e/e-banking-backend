package com.example.demo.dto;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "category")
public class Category {

    @Id
    private Long id;
    @ManyToOne
    @JoinColumn(name = "id")
    private List<Product> productList;
}

// dto et entity