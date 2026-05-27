package com.campusbites.admin.controller;

import com.campusbites.common.response.ApiResponse;
import com.campusbites.order.dto.OrderDtos;
import com.campusbites.order.model.Order;
import com.campusbites.order.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final OrderService orderService;
    public AdminController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping("/orders")
    public ResponseEntity<ApiResponse<Page<OrderDtos.OrderResponse>>> listOrders(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.getAllOrders(page, size)));
    }

    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<ApiResponse<OrderDtos.OrderResponse>> updateStatus(
            @PathVariable String id,
            @Valid @RequestBody OrderDtos.UpdateStatusRequest req) {
        return ResponseEntity.ok(ApiResponse.ok(orderService.updateStatus(id, req.status())));
    }

    @PostMapping("/products/upload")
    public ResponseEntity<ApiResponse<Map<String, String>>> uploadImage(
            @RequestBody Map<String, String> body) {
        String url = body.get("url");
        if (url == null || url.isBlank()) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.fail("BAD_REQUEST", "Field 'url' is required"));
        }
        return ResponseEntity.ok(ApiResponse.ok(Map.of("url", url)));
    }
}