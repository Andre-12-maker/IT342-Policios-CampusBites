package com.example.campusbites.features.auth.data.repository

import com.example.campusbites.core.session.SessionRepository
import com.example.campusbites.features.auth.data.api.AuthApi
import com.example.campusbites.features.auth.data.model.AuthResponse
import com.example.campusbites.features.auth.data.model.LoginRequest
import com.example.campusbites.features.auth.data.model.RegisterRequest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val api: AuthApi,
    private val session: SessionRepository,
) {
    suspend fun login(email: String, password: String): Result<AuthResponse> =
        runCatching {
            val envelope = api.login(LoginRequest(email, password))

            if (!envelope.success || envelope.data == null) {
                val msg = envelope.error?.message?.takeIf { it.isNotBlank() }
                    ?: "Login failed"
                error(msg)
            }

            persistSession(envelope.data)
            envelope.data
        }

    suspend fun register(name: String, email: String, password: String): Result<AuthResponse> =
        runCatching {
            val parts     = name.trim().split(" ")
            val firstname = parts.firstOrNull() ?: name
            val lastname  = parts.drop(1).joinToString(" ")

            val envelope = api.register(
                RegisterRequest(firstname, lastname, email, password)
            )

            if (!envelope.success || envelope.data == null) {
                val msg = envelope.error?.message?.takeIf { it.isNotBlank() }
                    ?: "Registration failed"
                error(msg)
            }

            persistSession(envelope.data)
            envelope.data
        }

    private suspend fun persistSession(auth: AuthResponse) {
        if (auth.accessToken.isBlank()) return
        val userJson = auth.user?.let { Json.encodeToString(it) } ?: "{}"
        session.save(auth.accessToken, userJson)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object AuthApiModule {
    @Provides @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi =
        retrofit.create(AuthApi::class.java)
}