package com.campusbites.order.controller;

import com.campusbites.common.response.ApiResponse;
import com.campusbites.order.dto.OrderDtos;
import com.campusbites.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<OrderDtos.OrderResponse>> checkout(
            @AuthenticationPrincipal UserDetails principal,
            @Valid @RequestBody OrderDtos.CheckoutRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok(orderService.placeOrder(principal.getUsername(), req)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<OrderDtos.OrderResponse>>> myOrders(
            @AuthenticationPrincipal UserDetails principal,
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(
                orderService.getMyOrders(principal.getUsername(), page, size)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderDtos.OrderResponse>> getOrder(
            @AuthenticationPrincipal UserDetails principal,
            @PathVariable String id) {
        return ResponseEntity.ok(ApiResponse.ok(
                orderService.getMyOrder(principal.getUsername(), id)));
    }
}