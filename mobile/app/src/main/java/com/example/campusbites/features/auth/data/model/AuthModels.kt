package com.example.campusbites.features.auth.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// ── Requests ──────────────────────────────────────────────────────────────────

@Serializable
data class LoginRequest(
    val email: String,
    val password: String,
)

@Serializable
data class RegisterRequest(
    val firstname: String,
    val lastname: String,
    val email: String,
    val password: String,
)

// ── Backend envelope: { "success": true, "data": T } ─────────────────────────

@Serializable
data class ApiResponse<T>(
    val success: Boolean = false,
    val data: T? = null,
    val error: ApiError? = null,
)

@Serializable
data class ApiError(
    val code: String = "",
    val message: String = "",
)

// ── Auth payload (inside data) ────────────────────────────────────────────────

@Serializable
data class AuthResponse(
    @SerialName("accessToken") val accessToken: String = "",
    val user: UserDto? = null,
)

@Serializable
data class UserDto(
    @SerialName("_id") val id: String = "",
    val firstname: String = "",
    val lastname: String = "",
    val email: String = "",
    val role: String = "",
) {
    val fullName get() = "$firstname $lastname".trim()
}