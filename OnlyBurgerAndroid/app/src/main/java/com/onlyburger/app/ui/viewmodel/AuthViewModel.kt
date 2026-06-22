package com.onlyburger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlyburger.app.data.repository.AuthRepository
import com.onlyburger.app.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false,
)

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _state = MutableStateFlow(AuthUiState())
    val state: StateFlow<AuthUiState> = _state.asStateFlow()

    fun login(usernameOrEmail: String, password: String) {
        if (_state.value.loading) return
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            runCatching { authRepository.login(usernameOrEmail.trim(), password) }
                .onSuccess { _state.update { s -> s.copy(loading = false, success = true) } }
                .onFailure { e -> _state.update { s -> s.copy(loading = false, error = e.toUserMessage()) } }
        }
    }

    fun register(username: String, email: String, password: String) {
        if (_state.value.loading) return
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            runCatching { authRepository.register(username.trim(), email.trim(), password) }
                .onSuccess { _state.update { s -> s.copy(loading = false, success = true) } }
                .onFailure { e -> _state.update { s -> s.copy(loading = false, error = e.toUserMessage()) } }
        }
    }

    fun consumeSuccess() = _state.update { it.copy(success = false) }
    fun clearError() = _state.update { it.copy(error = null) }
}
