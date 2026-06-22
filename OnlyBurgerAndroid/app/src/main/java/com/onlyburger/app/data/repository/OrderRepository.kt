package com.onlyburger.app.data.repository

import com.onlyburger.app.data.remote.ApiService
import com.onlyburger.app.data.remote.dto.CreateOrderRequest
import com.onlyburger.app.data.remote.dto.OrderDto

/** Creating and managing the signed-in user's orders. */
class OrderRepository(private val api: ApiService) {
    suspend fun createOrder(deliveryLocation: String): OrderDto =
        api.createOrder(CreateOrderRequest(deliveryLocation))

    suspend fun getMyOrders(): List<OrderDto> = api.getMyOrders()

    suspend fun payOrder(id: Int): OrderDto = api.payOrder(id)

    suspend fun deleteOrder(id: Int) = api.deleteOrder(id)
}
