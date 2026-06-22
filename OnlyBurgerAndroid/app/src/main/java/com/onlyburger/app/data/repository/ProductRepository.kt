package com.onlyburger.app.data.repository

import com.onlyburger.app.data.remote.ApiService
import com.onlyburger.app.data.remote.dto.ProductDto

/** Reads the menu from the backend. */
class ProductRepository(private val api: ApiService) {
    suspend fun getProducts(): List<ProductDto> = api.getProducts()
}
