package com.campusbites.order.webhook;

import com.campusbites.common.response.ApiResponse;
import com.campusbites.order.service.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhook")
public class StripeWebhookController {

    private static final Logger log = LoggerFactory.getLogger(StripeWebhookController.class);

    private final OrderService orderService;

    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    public StripeWebhookController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping(value = "/stripe", consumes = "application/json")
    public ResponseEntity<ApiResponse<Void>> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            log.warn("Invalid Stripe signature: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.fail("INVALID_SIGNATURE", "Webhook signature invalid"));
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session != null && "paid".equals(session.getPaymentStatus())) {
                try {
                    orderService.confirmOrder(session.getId());
                    log.info("Order confirmed for session: {}", session.getId());
                } catch (Exception e) {
                    log.error("Failed to confirm order for session {}: {}", session.getId(), e.getMessage());
                }
            }
        }

        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}