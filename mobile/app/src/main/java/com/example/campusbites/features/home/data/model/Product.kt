package com.example.campusbites.features.home.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    @SerialName("_id") val id: String = "",
    val name: String                  = "",
    val category: String              = "",
    val price: Double                 = 0.0,
    val description: String           = "",
    val image: String?                = null,
    val rating: Double                = 4.5,
)

@Serializable
data class ProductPageResponse(
    val data: ProductData? = null,
)

@Serializable
data class ProductData(
    val content: List<Product>? = null,
)