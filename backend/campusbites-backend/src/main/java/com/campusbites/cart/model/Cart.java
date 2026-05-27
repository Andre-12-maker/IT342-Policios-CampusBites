package com.campusbites.cart.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Data @NoArgsConstructor
@Document(collection = "carts")
public class Cart {
    @Id private String id;
    @Indexed(unique = true) private String userId;
    private List<CartItem> items = new ArrayList<>();
    @LastModifiedDate private Instant updatedAt;

    @Data @NoArgsConstructor
    public static class CartItem {
        private String productId;
        private String name;
        private BigDecimal price;
        private String imageUrl;
        private int qty;
    }
}