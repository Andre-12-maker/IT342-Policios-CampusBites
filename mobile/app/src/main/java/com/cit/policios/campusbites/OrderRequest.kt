package com.cit.policios.campusbites


// OrderRequest.kt
data class OrderRequest(
    val userId: String,
    val items: List<Map<String, Any>>,
    val totalAmount: Double,
    val deliveryAddress: String
)