package com.onlyburger.app.data.remote

import com.onlyburger.app.data.remote.dto.AddToCartRequest
import com.onlyburger.app.data.remote.dto.AuthResponse
import com.onlyburger.app.data.remote.dto.CartDto
import com.onlyburger.app.data.remote.dto.CreateOrderRequest
import com.onlyburger.app.data.remote.dto.LoginRequest
import com.onlyburger.app.data.remote.dto.OrderDto
import com.onlyburger.app.data.remote.dto.ProductDto
import com.onlyburger.app.data.remote.dto.RegisterRequest
import com.onlyburger.app.data.remote.dto.UpdateCartItemRequest
import com.onlyburger.app.data.remote.dto.UpdateOrderRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Retrofit description of the OnlyBurger HTTP API. Every call is a suspend function,
 * so it runs on a background dispatcher and returns the decoded body directly.
 */
interface ApiService {

    // Auth
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    // Products
    @GET("api/products")
    suspend fun getProducts(): List<ProductDto>

    @GET("api/products/{id}")
    suspend fun getProduct(@Path("id") id: Int): ProductDto

    // Cart
    @GET("api/cart")
    suspend fun getCart(): CartDto

    @POST("api/cart/items")
    suspend fun addToCart(@Body request: AddToCartRequest): CartDto

    @PUT("api/cart/items/{productId}")
    suspend fun updateCartItem(
        @Path("productId") productId: Int,
        @Body request: UpdateCartItemRequest,
    ): CartDto

    @DELETE("api/cart/items/{productId}")
    suspend fun removeCartItem(@Path("productId") productId: Int): CartDto

    // Orders (customer)
    @POST("api/orders")
    suspend fun createOrder(@Body request: CreateOrderRequest): OrderDto

    @GET("api/orders/mine")
    suspend fun getMyOrders(): List<OrderDto>

    @GET("api/orders/{id}")
    suspend fun getOrder(@Path("id") id: Int): OrderDto

    @PUT("api/orders/{id}")
    suspend fun updateOrder(
        @Path("id") id: Int,
        @Body request: UpdateOrderRequest,
    ): OrderDto

    @DELETE("api/orders/{id}")
    suspend fun deleteOrder(@Path("id") id: Int)

    @POST("api/orders/{id}/pay")
    suspend fun payOrder(@Path("id") id: Int): OrderDto
}
