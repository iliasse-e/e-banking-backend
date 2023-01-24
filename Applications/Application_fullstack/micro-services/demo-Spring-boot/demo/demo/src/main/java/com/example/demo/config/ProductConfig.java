package com.example.demo.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.demo.dto.Product;
import com.example.demo.repository.ProductRepository;

@Configuration
public class ProductConfig {

    @Bean
    CommandLineRunner commandLineRunner(ProductRepository repository) {
        return args -> {
            Product iPhone = Product.builder()
            .name("iPhone 13")
            .brand("Apple")
            .price(889)
            .promotion(true)
            .build();
            
            Product alcatel = Product.builder()
            .name("Alcatel 1")
            .brand("Alcatel Lucent")
            .price(49)
            .promotion(false)
            .build();

            repository.saveAll(
                List.of(iPhone, alcatel)
            );

            };
    }
    
}
