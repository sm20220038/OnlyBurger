package com.onlyburger.app.di

import android.content.Context
import com.onlyburger.app.data.TokenStore
import com.onlyburger.app.data.remote.NetworkModule
import com.onlyburger.app.data.repository.AuthRepository
import com.onlyburger.app.data.repository.CartRepository
import com.onlyburger.app.data.repository.OrderRepository
import com.onlyburger.app.data.repository.ProductRepository

/**
 * Tiny manual dependency-injection container. Holds the single instances of the
 * networking stack and repositories, created once and shared across the app.
 * (Kept manual on purpose to avoid extra build setup like Hilt/KSP.)
 */
class AppContainer(context: Context) {

    val tokenStore = TokenStore(context.applicationContext)

    private val apiService = NetworkModule.createApiService(tokenStore)

    val authRepository = AuthRepository(apiService, tokenStore)
    val productRepository = ProductRepository(apiService)
    val cartRepository = CartRepository(apiService)
    val orderRepository = OrderRepository(apiService)
}
