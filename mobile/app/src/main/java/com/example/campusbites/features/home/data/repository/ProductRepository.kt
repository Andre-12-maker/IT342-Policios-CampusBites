package com.example.campusbites.features.home.data.repository

import com.example.campusbites.features.home.data.api.ProductApi
import com.example.campusbites.features.home.data.model.MOCK_PRODUCTS
import com.example.campusbites.features.home.data.model.Product
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepository @Inject constructor(
    private val api: ProductApi,
) {
    suspend fun getProducts(category: String?, search: String?): List<Product> {
        return try {
            val resp = api.getProducts(
                category = if (category == "All" || category.isNullOrBlank()) null else category,
                search   = search?.takeIf { it.isNotBlank() },
            )
            val list = resp.data?.content
            if (!list.isNullOrEmpty()) list
            else fallback(category, search)
        } catch (e: Exception) {
            fallback(category, search)
        }
    }

    private fun fallback(category: String?, search: String?): List<Product> {
        var list = MOCK_PRODUCTS
        if (!category.isNullOrBlank() && category != "All")
            list = list.filter { it.category == category }
        if (!search.isNullOrBlank())
            list = list.filter { it.name.contains(search, ignoreCase = true) }
        return list
    }
}

@Module
@InstallIn(SingletonComponent::class)
object ProductApiModule {
    @Provides @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi =
        retrofit.create(ProductApi::class.java)
}