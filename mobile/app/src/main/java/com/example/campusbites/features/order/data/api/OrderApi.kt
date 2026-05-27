package com.example.campusbites.features.order.data.api

import com.example.campusbites.features.order.data.model.OrderListResponse
import com.example.campusbites.features.order.data.model.OrderResponse
import com.example.campusbites.features.order.data.model.PlaceOrderRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OrderApi {
    @POST("orders")
    suspend fun placeOrder(@Body body: PlaceOrderRequest): OrderResponse

    @GET("orders/user")
    suspend fun getUserOrders(): OrderListResponse
}