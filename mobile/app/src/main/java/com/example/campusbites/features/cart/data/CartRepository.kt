package com.example.campusbites.features.cart.data

import com.example.campusbites.features.home.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

data class CartItem(
    val product: Product,
    val qty: Int,
)

@Singleton
class CartRepository @Inject constructor() {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    val totalItems get() = _items.value.sumOf { it.qty }
    val totalPrice get() = _items.value.sumOf { it.product.price * it.qty }

    fun add(product: Product) {
        _items.update { list ->
            val existing = list.find { it.product.id == product.id }
            if (existing != null)
                list.map {
                    if (it.product.id == product.id) it.copy(qty = it.qty + 1) else it
                }
            else
                list + CartItem(product, 1)
        }
    }

    fun updateQty(productId: String, qty: Int) {
        _items.update { list ->
            if (qty <= 0) list.filter { it.product.id != productId }
            else list.map {
                if (it.product.id == productId) it.copy(qty = qty) else it
            }
        }
    }

    fun remove(productId: String) {
        _items.update { it.filter { item -> item.product.id != productId } }
    }

    fun clear() {
        _items.value = emptyList()
    }

    fun getQty(productId: String) =
        _items.value.find { it.product.id == productId }?.qty ?: 0
}