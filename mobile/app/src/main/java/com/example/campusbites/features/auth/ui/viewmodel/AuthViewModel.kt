package com.example.campusbites.features.auth.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campusbites.features.auth.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthUiState {
    object Idle    : AuthUiState()
    object Loading : AuthUiState()
    object Success : AuthUiState()
    data class Error(val message: String) : AuthUiState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repo: AuthRepository,
) : ViewModel() {

    private val _state = MutableStateFlow<AuthUiState>(AuthUiState.Idle)
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _state.value = AuthUiState.Error("Please fill in all fields")
            return
        }
        viewModelScope.launch {
            _state.value = AuthUiState.Loading
            repo.login(email, password).fold(
                onSuccess = { _state.value = AuthUiState.Success },
                onFailure = { _state.value = AuthUiState.Error(it.message ?: "Login failed") },
            )
        }
    }

    fun register(name: String, email: String, password: String) {
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            _state.value = AuthUiState.Error("Please fill in all fields")
            return
        }
        viewModelScope.launch {
            _state.value = AuthUiState.Loading
            repo.register(name, email, password).fold(
                onSuccess = { _state.value = AuthUiState.Success },
                onFailure = { _state.value = AuthUiState.Error(it.message ?: "Registration failed") },
            )
        }
    }

    fun resetState() { _state.value = AuthUiState.Idle }
}