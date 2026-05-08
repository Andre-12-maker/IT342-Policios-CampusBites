package com.cit.policios.campusbites.shared.network

import com.cit.policios.campusbites.features.auth.User
import com.cit.policios.campusbites.features.home.Food
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

object ApiClient {
    private const val BASE_URL = "http://10.0.2.2:8080"
    private val client = OkHttpClient()
    private val gson = Gson()
    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    private fun post(endpoint: String, jsonBody: String): String {
        val requestBody = jsonBody.toRequestBody(jsonMediaType)
        val request = Request.Builder()
            .url(BASE_URL + endpoint)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw Exception("API error: ${response.code} ${response.message}")
            }
            return response.body?.string() ?: throw Exception("Empty response body")
        }
    }

    fun login(email: String, password: String): Result<User> {
        return try {
            val jsonBody = gson.toJson(mapOf("email" to email, "password" to password))
            val responseBody = post("/auth/login", jsonBody)
            val user = gson.fromJson(responseBody, User::class.java)
            Result.success(user)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    fun register(user: User): Result<User> {
        return try {
            val jsonBody = gson.toJson(user)
            val responseBody = post("/auth/register", jsonBody)
            val savedUser = gson.fromJson(responseBody, User::class.java)
            Result.success(savedUser)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    fun getFoods(): Result<List<Food>> {
        return try {
            val request = Request.Builder()
                .url(BASE_URL + "/api/food")
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw Exception("HTTP ${response.code}")
                val body = response.body?.string() ?: throw Exception("Empty body")
                val type = object : com.google.gson.reflect.TypeToken<List<Food>>() {}.type
                Result.success(gson.fromJson(body, type))
            }
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    fun placeOrder(order: OrderRequest): Result<String> {
        return try {
            val json = gson.toJson(order)
            val body = post("/api/orders", json)
            Result.success(body)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}
