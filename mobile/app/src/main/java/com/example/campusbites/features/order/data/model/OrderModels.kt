package com.example.campusbites.features.order.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ── Place order ───────────────────────────────────────────────────────────────

@Serializable
data class PlaceOrderRequest(
    val items: List<OrderItemRequest>,
    val address: AddressRequest,
    val promoCode: String? = null,
)

@Serializable
data class OrderItemRequest(
    val productId: String,
    val quantity: Int,
    val price: Double,
)

@Serializable
data class AddressRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val street: String,
    val city: String,
    val state: String,
    val zipCode: String,
    val country: String,
    val phone: String,
)

// ── Order response ────────────────────────────────────────────────────────────

@Serializable
data class OrderResponse(
    @SerialName("_id") val id: String = "",
    val status: String = "Food processing",
    val amount: Double = 0.0,
    val items: List<OrderItemResponse> = emptyList(),
    val payment: Boolean = false,
)

@Serializable
data class OrderItemResponse(
    val productId: String = "",
    val name: String = "",
    val quantity: Int = 1,
    val price: Double = 0.0,
)

@Serializable
data class OrderListResponse(
    val data: List<OrderResponse>? = null,
    // some backends wrap in a different shape
    val orders: List<OrderResponse>? = null,
) {
    val resolved get() = data ?: orders ?: emptyList()
}