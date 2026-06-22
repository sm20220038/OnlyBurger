package com.onlyburger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlyburger.app.data.remote.dto.OrderDto
import com.onlyburger.app.data.repository.OrderRepository
import com.onlyburger.app.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrdersUiState(
    val loading: Boolean = true,
    val orders: List<OrderDto> = emptyList(),
    val error: String? = null,
    val message: String? = null,
)

class OrdersViewModel(private val orderRepository: OrderRepository) : ViewModel() {

    private val _state = MutableStateFlow(OrdersUiState())
    val state: StateFlow<OrdersUiState> = _state.asStateFlow()

    // Loaded by the screen on entry (see OrdersScreen) so the list refreshes after checkout.

    fun load() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            runCatching { orderRepository.getMyOrders() }
                .onSuccess { orders -> _state.update { it.copy(loading = false, orders = orders) } }
                .onFailure { e -> _state.update { it.copy(loading = false, error = e.toUserMessage()) } }
        }
    }

    fun pay(orderId: Int) {
        viewModelScope.launch {
            runCatching { orderRepository.payOrder(orderId) }
                .onSuccess { _state.update { it.copy(message = "Payment successful") }; load() }
                .onFailure { e -> _state.update { it.copy(error = e.toUserMessage()) } }
        }
    }

    fun cancel(orderId: Int) {
        viewModelScope.launch {
            runCatching { orderRepository.deleteOrder(orderId) }
                .onSuccess { _state.update { it.copy(message = "Order canceled") }; load() }
                .onFailure { e -> _state.update { it.copy(error = e.toUserMessage()) } }
        }
    }

    fun consumeMessage() = _state.update { it.copy(message = null) }
    fun clearError() = _state.update { it.copy(error = null) }
}
