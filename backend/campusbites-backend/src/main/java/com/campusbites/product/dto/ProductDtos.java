package com.campusbites.product.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductDtos {

    public record CreateProductRequest(
            @NotBlank(message = "Name is required") String name,
            @NotBlank(message = "Description is required") String description,
            @NotBlank(message = "Category is required") String category,
            @NotNull @DecimalMin(value = "0.01", message = "Price must be greater than 0") BigDecimal price,
            String imageUrl,
            @Min(0) int stock
    ) {}

    public record UpdateProductRequest(
            String name, String description, String category,
            BigDecimal price, String imageUrl, Integer stock, Boolean available
    ) {}

    public record ProductResponse(
            String id, String name, String description, String category,
            BigDecimal price, String imageUrl, double rating,
            int ratingCount, boolean available, int stock
    ) {}
}