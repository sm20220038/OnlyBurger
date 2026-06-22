package com.onlyburger.app.data.remote.dto

/**
 * Data transfer objects that mirror the JSON returned by the OnlyBurger backend.
 * Property names match the JSON field names exactly, so Gson maps them automatically.
 */

// ----- Auth -----
data class LoginRequest(
    val usernameOrEmail: String,
    val password: String,
)

data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
)

data class AuthResponse(
    val userId: Int,
    val username: String,
    val email: String,
    val role: String,
    val token: String,
    val expiresAtUtc: String,
)

// ----- Products -----
data class ProductDto(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
)

// ----- Cart -----
data class AddToCartRequest(
    val productId: Int,
    val quantity: Int,
)

data class UpdateCartItemRequest(
    val quantity: Int,
)

data class CartItemDto(
    val id: Int,
    val productId: Int,
    val productName: String,
    val unitPrice: Double,
    val quantity: Int,
    val lineTotal: Double,
)

data class CartDto(
    val items: List<CartItemDto> = emptyList(),
    val totalPrice: Double = 0.0,
)

// ----- Orders -----
data class CreateOrderRequest(
    val deliveryLocation: String,
)

data class OrderLineRequest(
    val productId: Int,
    val quantity: Int,
)

data class UpdateOrderRequest(
    val deliveryLocation: String,
    val items: List<OrderLineRequest>? = null,
)

data class OrderItemDto(
    val id: Int,
    val productId: Int,
    val productName: String,
    val quantity: Int,
    val unitPrice: Double,
    val lineTotal: Double,
)

data class OrderDto(
    val id: Int,
    val userId: Int,
    val deliveryLocation: String,
    val orderDateTime: String,
    val totalPrice: Double,
    val status: String,
    val paymentStatus: String,
    val items: List<OrderItemDto> = emptyList(),
)
