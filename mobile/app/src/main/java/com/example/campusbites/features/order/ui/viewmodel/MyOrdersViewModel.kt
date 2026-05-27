package com.example.campusbites.features.order.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusbites.features.order.data.model.OrderResponse
import com.example.campusbites.features.order.data.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class MyOrdersUiState {
    object Loading : MyOrdersUiState()
    data class Success(val orders: List<OrderResponse>) : MyOrdersUiState()
    data class Error(val message: String) : MyOrdersUiState()
}

@HiltViewModel
class MyOrdersViewModel @Inject constructor(
    private val repo: OrderRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<MyOrdersUiState>(MyOrdersUiState.Loading)
    val state: StateFlow<MyOrdersUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.value = MyOrdersUiState.Loading
            repo.getUserOrders().fold(
                onSuccess = { _state.value = MyOrdersUiState.Success(it) },
                onFailure = { _state.value = MyOrdersUiState.Error(it.message ?: "Failed to load orders") },
            )
        }
    }
}