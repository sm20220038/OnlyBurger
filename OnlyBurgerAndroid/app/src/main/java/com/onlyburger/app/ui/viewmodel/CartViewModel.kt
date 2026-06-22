package com.onlyburger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlyburger.app.data.remote.dto.CartDto
import com.onlyburger.app.data.repository.CartRepository
import com.onlyburger.app.data.repository.OrderRepository
import com.onlyburger.app.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CartUiState(
    val loading: Boolean = false,
    val cart: CartDto = CartDto(),
    val error: String? = null,
    val message: String? = null,
) {
    val itemCount: Int get() = cart.items.sumOf { it.quantity }
}

/**
 * Shared across the menu, cart and bottom-bar badge so the cart count stays in sync.
 * Every mutating call returns the updated cart from the backend, which we store directly.
 */
class CartViewModel(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(CartUiState())
    val state: StateFlow<CartUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            runCatching { cartRepository.getCart() }
                .onSuccess { cart -> _state.update { it.copy(loading = false, cart = cart) } }
                .onFailure { e -> _state.update { it.copy(loading = false, error = e.toUserMessage()) } }
        }
    }

    fun addToCart(productId: Int, productName: String, quantity: Int = 1) {
        viewModelScope.launch {
            runCatching { cartRepository.addToCart(productId, quantity) }
                .onSuccess { cart -> _state.update { it.copy(cart = cart, message = "Added $productName to cart") } }
                .onFailure { e -> _state.update { it.copy(error = e.toUserMessage()) } }
        }
    }

    fun updateQuantity(productId: Int, quantity: Int) {
        if (quantity < 1) return
        viewModelScope.launch {
            runCatching { cartRepository.updateItem(productId, quantity) }
                .onSuccess { cart -> _state.update { it.copy(cart = cart) } }
                .onFailure { e -> _state.update { it.copy(error = e.toUserMessage()) } }
        }
    }

    fun removeItem(productId: Int) {
        viewModelScope.launch {
            runCatching { cartRepository.removeItem(productId) }
                .onSuccess { cart -> _state.update { it.copy(cart = cart) } }
                .onFailure { e -> _state.update { it.copy(error = e.toUserMessage()) } }
        }
    }

    fun checkout(deliveryLocation: String, onPlaced: () -> Unit) {
        if (_state.value.loading) return
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            runCatching { orderRepository.createOrder(deliveryLocation.trim()) }
                .onSuccess {
                    // The order was created and the backend emptied the cart; refresh to reflect that.
                    runCatching { cartRepository.getCart() }
                        .onSuccess { cart -> _state.update { it.copy(loading = false, cart = cart) } }
                        .onFailure { _state.update { it.copy(loading = false, cart = CartDto()) } }
                    onPlaced()
                }
                .onFailure { e -> _state.update { it.copy(loading = false, error = e.toUserMessage()) } }
        }
    }

    fun clearOnLogout() = _state.update { CartUiState() }
    fun consumeMessage() = _state.update { it.copy(message = null) }
    fun clearError() = _state.update { it.copy(error = null) }
}
