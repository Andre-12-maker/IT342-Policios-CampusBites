package com.campusbites.product.controller;

import com.campusbites.common.response.ApiResponse;
import com.campusbites.product.dto.ProductDtos;
import com.campusbites.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private final ProductService productService;
    public ProductController(ProductService productService) { this.productService = productService; }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<Page<ProductDtos.ProductResponse>>> listProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(productService.getProducts(category, search, page, size)));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ApiResponse<ProductDtos.ProductResponse>> getProduct(@PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(productService.getProduct(id)));
    }

    @PostMapping("/admin/products")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDtos.ProductResponse>> createProduct(@Valid @RequestBody ProductDtos.CreateProductRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(productService.createProduct(req)));
    }

    @PutMapping("/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductDtos.ProductResponse>> updateProduct(@PathVariable String id, @RequestBody ProductDtos.UpdateProductRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(productService.updateProduct(id, req)));
    }

    @DeleteMapping("/admin/products/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}