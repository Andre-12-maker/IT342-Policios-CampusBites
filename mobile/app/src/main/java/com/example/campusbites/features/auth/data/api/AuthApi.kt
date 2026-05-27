package com.example.campusbites.features.auth.data.api

import com.example.campusbites.features.auth.data.model.ApiResponse
import com.example.campusbites.features.auth.data.model.AuthResponse
import com.example.campusbites.features.auth.data.model.LoginRequest
import com.example.campusbites.features.auth.data.model.RegisterRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body body: LoginRequest): ApiResponse<AuthResponse>

    @POST("auth/register")
    suspend fun register(@Body body: RegisterRequest): ApiResponse<AuthResponse>
}