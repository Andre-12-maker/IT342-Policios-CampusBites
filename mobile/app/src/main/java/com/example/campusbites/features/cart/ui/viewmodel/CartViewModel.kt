package com.example.campusbites.features.cart.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusbites.features.cart.data.CartItem
import com.example.campusbites.features.cart.data.CartRepository
import com.example.campusbites.features.order.data.model.AddressRequest
import com.example.campusbites.features.order.data.model.OrderItemRequest
import com.example.campusbites.features.order.data.model.OrderResponse
import com.example.campusbites.features.order.data.model.PlaceOrderRequest
import com.example.campusbites.features.order.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class OrderUiState {
    object Idle    : OrderUiState()
    object Loading : OrderUiState()
    data class Success(val order: OrderResponse) : OrderUiState()
    data class Error(val message: String) : OrderUiState()
}

@HiltViewModel
class CartViewModel @Inject constructor(
    private val cartRepo: CartRepository,
    private val orderRepo: OrderRepository,
) : ViewModel() {

    val cartItems: StateFlow<List<CartItem>> = cartRepo.items.stateIn(
        viewModelScope, SharingStarted.Eagerly, emptyList()
    )

    val subtotal get() = cartRepo.totalPrice
    val deliveryFee = 2.0
    val total get() = subtotal + deliveryFee

    private val _orderState = MutableStateFlow<OrderUiState>(OrderUiState.Idle)
    val orderState: StateFlow<OrderUiState> = _orderState.asStateFlow()

    fun increment(productId: String) {
        val qty = cartRepo.getQty(productId)
        cartRepo.updateQty(productId, qty + 1)
    }

    fun decrement(productId: String) {
        val qty = cartRepo.getQty(productId)
        cartRepo.updateQty(productId, qty - 1)
    }

    fun remove(productId: String) = cartRepo.remove(productId)

    fun placeOrder(
        firstName: String, lastName: String, email: String,
        street: String, city: String, state: String,
        zipCode: String, country: String, phone: String,
        promoCode: String = "",
    ) {
        val items = cartItems.value
        if (items.isEmpty()) {
            _orderState.value = OrderUiState.Error("Your cart is empty")
            return
        }
        viewModelScope.launch {
            _orderState.value = OrderUiState.Loading
            val request = PlaceOrderRequest(
                items = items.map {
                    OrderItemRequest(it.product.id, it.qty, it.product.price)
                },
                address = AddressRequest(
                    firstName, lastName, email,
                    street, city, state, zipCode, country, phone,
                ),
                promoCode = promoCode.takeIf { it.isNotBlank() },
            )
            orderRepo.placeOrder(request).fold(
                onSuccess = {
                    cartRepo.clear()
                    _orderState.value = OrderUiState.Success(it)
                },
                onFailure = {
                    _orderState.value = OrderUiState.Error(it.message ?: "Order failed")
                },
            )
        }
    }

    fun resetOrderState() { _orderState.value = OrderUiState.Idle }
}