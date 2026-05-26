package com.campusbites.cart.controller;

import com.campusbites.cart.dto.CartDtos;
import com.campusbites.cart.service.CartService;
import com.campusbites.common.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
public class CartController {

    private final CartService cartService;
    public CartController(CartService cartService) { this.cartService = cartService; }

    @GetMapping
    public ResponseEntity<ApiResponse<CartDtos.CartResponse>> getCart(@AuthenticationPrincipal UserDetails p) {
        return ResponseEntity.ok(ApiResponse.ok(cartService.getCart(p.getUsername())));
    }

    @PostMapping("/items")
    public ResponseEntity<ApiResponse<CartDtos.CartResponse>> addItem(@AuthenticationPrincipal UserDetails p, @Valid @RequestBody CartDtos.AddItemRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(cartService.addItem(p.getUsername(), req)));
    }

    @PutMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDtos.CartResponse>> updateItem(@AuthenticationPrincipal UserDetails p, @PathVariable String productId, @Valid @RequestBody CartDtos.UpdateItemRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(cartService.updateItem(p.getUsername(), productId, req)));
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<ApiResponse<CartDtos.CartResponse>> removeItem(@AuthenticationPrincipal UserDetails p, @PathVariable String productId) {
        return ResponseEntity.ok(ApiResponse.ok(cartService.removeItem(p.getUsername(), productId)));
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> clearCart(@AuthenticationPrincipal UserDetails p) {
        cartService.clearCart(p.getUsername());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}