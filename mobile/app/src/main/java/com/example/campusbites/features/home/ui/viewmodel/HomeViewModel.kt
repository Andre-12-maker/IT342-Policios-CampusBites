package com.example.campusbites.features.home.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusbites.features.cart.data.CartRepository
import com.example.campusbites.features.home.data.model.Product
import com.example.campusbites.features.home.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val products: List<Product>) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val productRepo: ProductRepository,
    private val cartRepo: CartRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _selectedCategory = MutableStateFlow("All")
    val selectedCategory: StateFlow<String> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val cartItems = cartRepo.items.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        emptyList(),
    )

    init { loadProducts() }

    fun selectCategory(cat: String) {
        _selectedCategory.value = cat
        loadProducts()
    }

    fun onSearch(q: String) {
        _searchQuery.value = q
        loadProducts()
    }

    fun addToCart(product: Product)         = cartRepo.add(product)
    fun updateCartQty(id: String, qty: Int) = cartRepo.updateQty(id, qty)
    fun getQty(id: String)                  = cartRepo.getQty(id)
    fun totalCartItems()                    = cartRepo.totalItems

    private fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = HomeUiState.Loading
            val products = productRepo.getProducts(
                category = _selectedCategory.value,
                search   = _searchQuery.value,
            )
            _uiState.value = HomeUiState.Success(products)
        }
    }
}