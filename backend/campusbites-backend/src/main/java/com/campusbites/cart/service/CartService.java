package com.campusbites.cart.service;

import com.campusbites.cart.dto.CartDtos;
import com.campusbites.cart.model.Cart;
import com.campusbites.cart.repository.CartRepository;
import com.campusbites.common.exception.AppException;
import com.campusbites.product.model.Product;
import com.campusbites.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;

    public CartService(CartRepository cartRepository, ProductService productService) {
        this.cartRepository = cartRepository;
        this.productService = productService;
    }

    public CartDtos.CartResponse getCart(String userId) { return toResponse(getOrCreateCart(userId)); }

    public CartDtos.CartResponse addItem(String userId, CartDtos.AddItemRequest req) {
        Product product = productService.findById(req.productId());
        if (!product.isAvailable()) throw AppException.badRequest("Product is unavailable");
        Cart cart = getOrCreateCart(userId);
        cart.getItems().stream().filter(i -> i.getProductId().equals(req.productId())).findFirst()
                .ifPresentOrElse(
                        item -> item.setQty(item.getQty() + req.qty()),
                        () -> {
                            Cart.CartItem n = new Cart.CartItem();
                            n.setProductId(product.getId()); n.setName(product.getName());
                            n.setPrice(product.getPrice()); n.setImageUrl(product.getImageUrl());
                            n.setQty(req.qty());
                            cart.getItems().add(n);
                        });
        return toResponse(cartRepository.save(cart));
    }

    public CartDtos.CartResponse updateItem(String userId, String productId, CartDtos.UpdateItemRequest req) {
        Cart cart = getOrCreateCart(userId);
        if (req.qty() == 0) {
            cart.getItems().removeIf(i -> i.getProductId().equals(productId));
        } else {
            cart.getItems().stream().filter(i -> i.getProductId().equals(productId)).findFirst()
                    .orElseThrow(() -> AppException.notFound("Item not in cart")).setQty(req.qty());
        }
        return toResponse(cartRepository.save(cart));
    }

    public CartDtos.CartResponse removeItem(String userId, String productId) {
        Cart cart = getOrCreateCart(userId);
        if (!cart.getItems().removeIf(i -> i.getProductId().equals(productId)))
            throw AppException.notFound("Item not in cart");
        return toResponse(cartRepository.save(cart));
    }

    public void clearCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    public Cart getOrCreateCart(String userId) {
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart c = new Cart(); c.setUserId(userId); return cartRepository.save(c);
        });
    }

    private CartDtos.CartResponse toResponse(Cart cart) {
        List<CartDtos.CartItemResponse> items = cart.getItems().stream()
                .map(i -> new CartDtos.CartItemResponse(i.getProductId(), i.getName(), i.getPrice(),
                        i.getImageUrl(), i.getQty(), i.getPrice().multiply(BigDecimal.valueOf(i.getQty()))))
                .toList();
        BigDecimal total = items.stream().map(CartDtos.CartItemResponse::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        int totalItems   = items.stream().mapToInt(CartDtos.CartItemResponse::qty).sum();
        return new CartDtos.CartResponse(cart.getId(), items, total, totalItems);
    }
}