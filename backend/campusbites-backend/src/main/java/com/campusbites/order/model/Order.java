package com.campusbites.order.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data @NoArgsConstructor
@Document(collection = "orders")
public class Order {
    @Id private String id;
    @Indexed private String userId;
    private List<OrderItem> items;
    private DeliveryInfo    deliveryInfo;
    private BigDecimal      subtotal;
    private BigDecimal      deliveryFee;
    private BigDecimal      total;
    private Status          status = Status.PENDING_PAYMENT;
    @Indexed(unique = true, sparse = true) private String stripeSessionId;
    @CreatedDate      private Instant createdAt;
    @LastModifiedDate private Instant updatedAt;

    @Data @NoArgsConstructor
    public static class OrderItem {
        private String productId, name;
        private BigDecimal price, subtotal;
        private int qty;
    }

    @Data @NoArgsConstructor
    public static class DeliveryInfo {
        private String firstName, lastName, email, phone, street, city, state, zipCode, country;
    }

    public enum Status { PENDING_PAYMENT, FOOD_PROCESSING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED }
}