package com.onlyburger.app.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.onlyburger.app.OnlyBurgerApp
import com.onlyburger.app.di.AppContainer

/**
 * Constructs the app's ViewModels with their repository dependencies pulled from the
 * [AppContainer]. Used instead of Hilt to keep the build configuration minimal.
 */
class AppViewModelFactory(private val container: AppContainer) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AuthViewModel::class.java) ->
            AuthViewModel(container.authRepository) as T

        modelClass.isAssignableFrom(MenuViewModel::class.java) ->
            MenuViewModel(container.productRepository) as T

        modelClass.isAssignableFrom(CartViewModel::class.java) ->
            CartViewModel(container.cartRepository, container.orderRepository) as T

        modelClass.isAssignableFrom(OrdersViewModel::class.java) ->
            OrdersViewModel(container.orderRepository) as T

        else -> throw IllegalArgumentException("Unknown ViewModel: ${modelClass.name}")
    }
}

/** Convenience for screens: builds a factory backed by the running app's container. */
@Composable
fun rememberAppViewModelFactory(): ViewModelProvider.Factory {
    val container = (LocalContext.current.applicationContext as OnlyBurgerApp).container
    return remember(container) { AppViewModelFactory(container) }
}
