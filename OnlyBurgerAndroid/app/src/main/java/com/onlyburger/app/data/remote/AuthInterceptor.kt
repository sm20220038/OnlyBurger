package com.onlyburger.app.data.remote

import com.onlyburger.app.data.TokenStore
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Attaches the stored JWT as a Bearer token to every outgoing request. Endpoints that
 * do not require auth (login, register, browsing the menu) simply ignore it.
 */
class AuthInterceptor(private val tokenStore: TokenStore) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = tokenStore.token
        val request = if (!token.isNullOrBlank()) {
            chain.request().newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
        } else {
            chain.request()
        }
        return chain.proceed(request)
    }
}
