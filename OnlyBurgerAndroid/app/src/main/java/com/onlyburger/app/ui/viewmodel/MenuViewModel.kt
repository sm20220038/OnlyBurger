package com.onlyburger.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.onlyburger.app.data.remote.dto.ProductDto
import com.onlyburger.app.data.repository.ProductRepository
import com.onlyburger.app.util.toUserMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MenuUiState(
    val loading: Boolean = true,
    val products: List<ProductDto> = emptyList(),
    val error: String? = null,
)

class MenuViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            runCatching { productRepository.getProducts() }
                .onSuccess { products -> _state.update { it.copy(loading = false, products = products) } }
                .onFailure { e -> _state.update { it.copy(loading = false, error = e.toUserMessage()) } }
        }
    }
}
