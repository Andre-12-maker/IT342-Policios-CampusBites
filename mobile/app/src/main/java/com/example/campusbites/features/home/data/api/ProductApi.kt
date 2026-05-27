package com.example.campusbites.features.home.data.api

import com.example.campusbites.features.home.data.model.ProductPageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApi {
    @GET("products")
    suspend fun getProducts(
        @Query("category") category: String? = null,
        @Query("search")   search: String?   = null,
        @Query("page")     page: Int         = 0,
        @Query("size")     size: Int         = 40,
    ): ProductPageResponse
}