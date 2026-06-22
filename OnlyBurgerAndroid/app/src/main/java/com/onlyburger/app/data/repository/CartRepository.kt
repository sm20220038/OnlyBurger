package com.onlyburger.app.data.repository

import com.onlyburger.app.data.remote.ApiService
import com.onlyburger.app.data.remote.dto.AddToCartRequest
import com.onlyburger.app.data.remote.dto.CartDto
import com.onlyburger.app.data.remote.dto.UpdateCartItemRequest

/** Operations on the signed-in user's shopping cart. Each call returns the updated cart. */
class CartRepository(private val api: ApiService) {
    suspend fun getCart(): CartDto = api.getCart()

    suspend fun addToCart(productId: Int, quantity: Int): CartDto =
        api.addToCart(AddToCartRequest(productId, quantity))

    suspend fun updateItem(productId: Int, quantity: Int): CartDto =
        api.updateCartItem(productId, UpdateCartItemRequest(quantity))

    suspend fun removeItem(productId: Int): CartDto =
        api.removeCartItem(productId)
}
