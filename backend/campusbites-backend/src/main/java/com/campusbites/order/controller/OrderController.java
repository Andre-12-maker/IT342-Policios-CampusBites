package com.campusbites.order.controller;

import com.campusbites.common.response.ApiResponse;
import com.campusbites.order.dto.OrderDtos;
import com.campusbites.order.model.Order;
import com.campusbites.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping("/orders/checkout-session")
    public ResponseEntity<ApiResponse<OrderDtos.CheckoutResponse>> checkout(
            @AuthenticationPrincipal UserDetails p, @Valid @RequestBody OrderDtos.CheckoutRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.createCheckoutSession(p.getUsername(), req)));
    }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<Page<OrderDtos.OrderResponse>>> myOrders(
            @AuthenticationPrincipal UserDetails p,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getUserOrders(p.getUsername(), page, size)));
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<ApiResponse<OrderDtos.OrderResponse>> getOrder(
            @AuthenticationPrincipal UserDetails p, @PathVariable String id) {
        boolean isAdmin = p.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        return ResponseEntity.ok(ApiResponse.ok(orderService.getOrder(id, p.getUsername(), isAdmin)));
    }

    @GetMapping("/admin/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<OrderDtos.OrderResponse>>> allOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getAllOrders(page, size)));
    }

    @PatchMapping("/admin/orders/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<OrderDtos.OrderResponse>> updateStatus(
            @PathVariable String id, @Valid @RequestBody OrderDtos.UpdateStatusRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.updateStatus(id, req.status())));
    }
}