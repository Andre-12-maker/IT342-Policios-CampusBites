package com.campusbites.product.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id private String id;
    private String     name;
    private String     description;
    private String     category;
    private BigDecimal price;
    private String     imageUrl;
    private double     rating      = 0.0;
    private int        ratingCount = 0;
    private boolean    available   = true;
    private int        stock       = 0;
    @CreatedDate      private Instant createdAt;
    @LastModifiedDate private Instant updatedAt;
}