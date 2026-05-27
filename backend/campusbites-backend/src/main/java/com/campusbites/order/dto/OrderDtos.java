package com.campusbites.order.dto;

import com.campusbites.order.model.Order;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class OrderDtos {

    public record CheckoutRequest(
            @NotNull @Valid DeliveryInfoRequest deliveryInfo
    ) {}

    public record DeliveryInfoRequest(
            @NotBlank String firstName, @NotBlank String lastName,
            @NotBlank @Email String email, @NotBlank String phone,
            @NotBlank String street, @NotBlank String city,
            String state, @NotBlank String zipCode, @NotBlank String country
    ) {}

    public record UpdateStatusRequest(@NotNull Order.Status status) {}

    public record OrderItemResponse(String productId, String name, BigDecimal price, int qty, BigDecimal subtotal) {}

    public record OrderResponse(
            String id, String userId, List<OrderItemResponse> items,
            DeliveryInfoRequest deliveryInfo, BigDecimal subtotal,
            BigDecimal deliveryFee, BigDecimal total, String status, Instant createdAt
    ) {}
}