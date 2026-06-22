package com.onlyburger.app.data.repository

import com.onlyburger.app.data.TokenStore
import com.onlyburger.app.data.remote.ApiService
import com.onlyburger.app.data.remote.dto.AuthResponse
import com.onlyburger.app.data.remote.dto.LoginRequest
import com.onlyburger.app.data.remote.dto.RegisterRequest

/**
 * Handles authentication and the local session. On success it stores the JWT via
 * [TokenStore] so subsequent requests are authenticated.
 */
class AuthRepository(
    private val api: ApiService,
    private val tokenStore: TokenStore,
) {
    val isLoggedIn: Boolean get() = tokenStore.isLoggedIn
    val username: String? get() = tokenStore.username
    val isAdmin: Boolean get() = tokenStore.isAdmin

    suspend fun login(usernameOrEmail: String, password: String): AuthResponse {
        val auth = api.login(LoginRequest(usernameOrEmail, password))
        tokenStore.save(auth)
        return auth
    }

    suspend fun register(username: String, email: String, password: String): AuthResponse {
        val auth = api.register(RegisterRequest(username, email, password))
        tokenStore.save(auth)
        return auth
    }

    fun logout() = tokenStore.clear()
}
