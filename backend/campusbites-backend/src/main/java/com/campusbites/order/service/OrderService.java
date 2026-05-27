package com.campusbites.order.service;

import com.campusbites.cart.service.CartService;
import com.campusbites.common.exception.AppException;
import com.campusbites.order.dto.OrderDtos;
import com.campusbites.order.model.Order;
import com.campusbites.order.repository.OrderRepository;
import com.campusbites.product.model.Product;
import com.campusbites.product.repository.ProductRepository;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class OrderService {

    private static final BigDecimal DELIVERY_FEE = new BigDecimal("2.00");

    private final OrderRepository   orderRepository;
    private final CartService       cartService;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository,
                        CartService cartService,
                        ProductRepository productRepository) {
        this.orderRepository   = orderRepository;
        this.cartService       = cartService;
        this.productRepository = productRepository;
    }

    public OrderDtos.OrderResponse placeOrder(String userId, OrderDtos.CheckoutRequest req) {
        var cart = cartService.getOrCreateCart(userId);
        if (cart.getItems().isEmpty()) throw AppException.badRequest("Cart is empty");

        List<Order.OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> AppException.notFound("Product not found: " + cartItem.getProductId()));
            if (product.getStock() < cartItem.getQty())
                throw AppException.badRequest("Insufficient stock for: " + product.getName());

            product.setStock(product.getStock() - cartItem.getQty());
            if (product.getStock() == 0) product.setAvailable(false);
            productRepository.save(product);

            Order.OrderItem item = new Order.OrderItem();
            item.setProductId(cartItem.getProductId());
            item.setName(cartItem.getName());
            item.setPrice(cartItem.getPrice());
            item.setQty(cartItem.getQty());
            item.setSubtotal(cartItem.getPrice().multiply(BigDecimal.valueOf(cartItem.getQty())));
            return item;
        }).toList();

        BigDecimal subtotal = orderItems.stream()
                .map(Order.OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        var d = req.deliveryInfo();
        Order.DeliveryInfo delivery = new Order.DeliveryInfo();
        delivery.setFirstName(d.firstName());
        delivery.setLastName(d.lastName());
        delivery.setEmail(d.email());
        delivery.setPhone(d.phone());
        delivery.setStreet(d.street());
        delivery.setCity(d.city());
        delivery.setState(d.state());
        delivery.setZipCode(d.zipCode());
        delivery.setCountry(d.country());

        Order order = new Order();
        order.setUserId(userId);
        order.setItems(orderItems);
        order.setDeliveryInfo(delivery);
        order.setSubtotal(subtotal);
        order.setDeliveryFee(DELIVERY_FEE);
        order.setTotal(subtotal.add(DELIVERY_FEE));
        order.setStatus(Order.Status.FOOD_PROCESSING);

        order = orderRepository.save(order);
        cartService.clearCart(userId);
        return toResponse(order);
    }

    public Page<OrderDtos.OrderResponse> getMyOrders(String userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(this::toResponse);
    }

    public OrderDtos.OrderResponse getMyOrder(String userId, String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> AppException.notFound("Order not found"));
        if (!order.getUserId().equals(userId))
            throw AppException.forbidden("Access denied");
        return toResponse(order);
    }

    public Page<OrderDtos.OrderResponse> getAllOrders(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAllByOrderByCreatedAtDesc(pageable)
                .map(this::toResponse);
    }

    public OrderDtos.OrderResponse updateStatus(String orderId, Order.Status status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> AppException.notFound("Order not found"));

        if (status == Order.Status.CANCELLED && order.getStatus() != Order.Status.CANCELLED) {
            order.getItems().forEach(item ->
                    productRepository.findById(item.getProductId()).ifPresent(product -> {
                        product.setStock(product.getStock() + item.getQty());
                        product.setAvailable(true);
                        productRepository.save(product);
                    })
            );
        }

        order.setStatus(status);
        return toResponse(orderRepository.save(order));
    }

    private OrderDtos.OrderResponse toResponse(Order order) {
        List<OrderDtos.OrderItemResponse> items = order.getItems().stream()
                .map(i -> new OrderDtos.OrderItemResponse(
                        i.getProductId(), i.getName(), i.getPrice(), i.getQty(), i.getSubtotal()))
                .toList();

        var d = order.getDeliveryInfo();
        OrderDtos.DeliveryInfoRequest delivery = new OrderDtos.DeliveryInfoRequest(
                d.getFirstName(), d.getLastName(), d.getEmail(), d.getPhone(),
                d.getStreet(), d.getCity(), d.getState(), d.getZipCode(), d.getCountry());

        return new OrderDtos.OrderResponse(
                order.getId(), order.getUserId(), items, delivery,
                order.getSubtotal(), order.getDeliveryFee(), order.getTotal(),
                order.getStatus().name(), order.getCreatedAt());
    }
}