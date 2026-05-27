package com.campusbites.cart.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class CartDtos {

    public record AddItemRequest(
            @NotBlank(message = "Product ID is required") String productId,
            @NotNull @Min(1) int qty
    ) {}

    public record UpdateItemRequest(@NotNull @Min(0) int qty) {}

    public record CartItemResponse(String productId, String name, BigDecimal price, String imageUrl, int qty, BigDecimal subtotal) {}

    public record CartResponse(String id, List<CartItemResponse> items, BigDecimal totalPrice, int totalItems) {}
}