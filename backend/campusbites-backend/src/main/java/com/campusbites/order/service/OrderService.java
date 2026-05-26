package com.campusbites.order.service;

import com.campusbites.cart.model.Cart;
import com.campusbites.cart.service.CartService;
import com.campusbites.common.exception.AppException;
import com.campusbites.order.dto.OrderDtos;
import com.campusbites.order.model.Order;
import com.campusbites.order.repository.OrderRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final BigDecimal DELIVERY_FEE = new BigDecimal("100.00");

    private final OrderRepository orderRepository;
    private final CartService     cartService;

    @Value("${stripe.currency}")
    private String currency;

    public OrderService(OrderRepository orderRepository, CartService cartService) {
        this.orderRepository = orderRepository;
        this.cartService     = cartService;
    }

    public OrderDtos.CheckoutResponse createCheckoutSession(String userId, OrderDtos.CheckoutRequest req) {
        Cart cart = cartService.getOrCreateCart(userId);
        if (cart.getItems().isEmpty()) throw AppException.badRequest("Your cart is empty");

        List<SessionCreateParams.LineItem> lineItems = cart.getItems().stream()
                .map(item -> SessionCreateParams.LineItem.builder()
                        .setQuantity((long) item.getQty())
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(currency)
                                .setUnitAmount(item.getPrice().multiply(BigDecimal.valueOf(100)).longValue())
                                .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                        .setName(item.getName()).build())
                                .build())
                        .build())
                .collect(Collectors.toCollection(ArrayList::new));

        lineItems.add(SessionCreateParams.LineItem.builder().setQuantity(1L)
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(currency)
                        .setUnitAmount(DELIVERY_FEE.multiply(BigDecimal.valueOf(100)).longValue())
                        .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName("Delivery Fee").build())
                        .build())
                .build());

        Order order = buildPendingOrder(userId, cart, req);

        try {
            Session session = Session.create(SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(req.successUrl() + "&session_id={CHECKOUT_SESSION_ID}")
                    .setCancelUrl(req.cancelUrl())
                    .addAllLineItem(lineItems)
                    .putMetadata("orderId", order.getId())
                    .build());
            order.setStripeSessionId(session.getId());
            orderRepository.save(order);
            return new OrderDtos.CheckoutResponse(session.getUrl());
        } catch (StripeException e) {
            throw new RuntimeException("Stripe error: " + e.getMessage(), e);
        }
    }

    public void confirmOrder(String stripeSessionId) {
        Order order = orderRepository.findByStripeSessionId(stripeSessionId)
                .orElseThrow(() -> AppException.notFound("Order not found for session: " + stripeSessionId));
        order.setStatus(Order.Status.FOOD_PROCESSING);
        orderRepository.save(order);
        cartService.clearCart(order.getUserId());
    }

    public Page<OrderDtos.OrderResponse> getUserOrders(String userId, int page, int size) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId, PageRequest.of(page, size)).map(this::toResponse);
    }

    public OrderDtos.OrderResponse getOrder(String orderId, String userId, boolean isAdmin) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> AppException.notFound("Order not found"));
        if (!isAdmin && !order.getUserId().equals(userId)) throw AppException.forbidden("Access denied");
        return toResponse(order);
    }

    public Page<OrderDtos.OrderResponse> getAllOrders(int page, int size) {
        return orderRepository.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size)).map(this::toResponse);
    }

    public OrderDtos.OrderResponse updateStatus(String orderId, Order.Status newStatus) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> AppException.notFound("Order not found"));
        order.setStatus(newStatus);
        return toResponse(orderRepository.save(order));
    }

    private Order buildPendingOrder(String userId, Cart cart, OrderDtos.CheckoutRequest req) {
        BigDecimal subtotal = cart.getItems().stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<Order.OrderItem> items = cart.getItems().stream().map(i -> {
            Order.OrderItem oi = new Order.OrderItem();
            oi.setProductId(i.getProductId()); oi.setName(i.getName());
            oi.setPrice(i.getPrice()); oi.setQty(i.getQty());
            oi.setSubtotal(i.getPrice().multiply(BigDecimal.valueOf(i.getQty())));
            return oi;
        }).toList();

        Order.DeliveryInfo di = new Order.DeliveryInfo();
        di.setFirstName(req.deliveryInfo().firstName()); di.setLastName(req.deliveryInfo().lastName());
        di.setEmail(req.deliveryInfo().email());         di.setPhone(req.deliveryInfo().phone());
        di.setStreet(req.deliveryInfo().street());       di.setCity(req.deliveryInfo().city());
        di.setState(req.deliveryInfo().state());         di.setZipCode(req.deliveryInfo().zipCode());
        di.setCountry(req.deliveryInfo().country());

        Order order = new Order();
        order.setUserId(userId); order.setItems(items); order.setDeliveryInfo(di);
        order.setSubtotal(subtotal); order.setDeliveryFee(DELIVERY_FEE);
        order.setTotal(subtotal.add(DELIVERY_FEE)); order.setStatus(Order.Status.PENDING_PAYMENT);
        return orderRepository.save(order);
    }

    private OrderDtos.OrderResponse toResponse(Order o) {
        List<OrderDtos.OrderItemResponse> items = o.getItems().stream()
                .map(i -> new OrderDtos.OrderItemResponse(i.getProductId(), i.getName(), i.getPrice(), i.getQty(), i.getSubtotal()))
                .toList();
        Order.DeliveryInfo di = o.getDeliveryInfo();
        return new OrderDtos.OrderResponse(o.getId(), o.getUserId(), items,
                new OrderDtos.DeliveryInfoRequest(di.getFirstName(), di.getLastName(), di.getEmail(),
                        di.getPhone(), di.getStreet(), di.getCity(), di.getState(), di.getZipCode(), di.getCountry()),
                o.getSubtotal(), o.getDeliveryFee(), o.getTotal(), o.getStatus().name(), o.getCreatedAt());
    }
}