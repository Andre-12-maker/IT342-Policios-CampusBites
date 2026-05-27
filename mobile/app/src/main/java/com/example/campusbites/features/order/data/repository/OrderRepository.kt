package com.example.campusbites.features.order.data.repository

import com.example.campusbites.features.order.data.api.OrderApi
import com.example.campusbites.features.order.data.model.OrderResponse
import com.example.campusbites.features.order.data.model.PlaceOrderRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepository @Inject constructor(
    private val api: OrderApi,
) {
    suspend fun placeOrder(request: PlaceOrderRequest): Result<OrderResponse> =
        runCatching { api.placeOrder(request) }

    suspend fun getUserOrders(): Result<List<OrderResponse>> =
        runCatching { api.getUserOrders().resolved }
}

@Module
@InstallIn(SingletonComponent::class)
object OrderApiModule {
    @Provides @Singleton
    fun provideOrderApi(retrofit: Retrofit): OrderApi =
        retrofit.create(OrderApi::class.java)
}