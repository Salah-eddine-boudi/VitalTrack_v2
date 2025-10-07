package com.salaheddine.vitaltrack.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed interface AuthState {
    object Idle : AuthState
    object Loading : AuthState
    data class Success(val user: AppUser) : AuthState
    data class Error(val message: String) : AuthState
}

class AuthViewModel : ViewModel() {
    private val _state = MutableStateFlow<AuthState>(AuthState.Idle)
    val state: StateFlow<AuthState> = _state

    fun checkSession() {
        AuthService.currentUser()?.let { _state.value = AuthState.Success(it) }
    }

    fun register(email: String, password: String) = viewModelScope.launch {
        _state.value = AuthState.Loading
        runCatching { AuthService.register(email, password) }
            .onSuccess { _state.value = AuthState.Success(it) }
            .onFailure { _state.value = AuthState.Error(it.message ?: "Erreur inscription") }
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        _state.value = AuthState.Loading
        runCatching { AuthService.login(email, password) }
            .onSuccess { _state.value = AuthState.Success(it) }
            .onFailure { _state.value = AuthState.Error(it.message ?: "Erreur connexion") }
    }

    fun logout() {
        AuthService.logout()
        _state.value = AuthState.Idle
    }
}
