package com.yundin.core.repository

import com.yundin.core.model.Product
import com.yundin.core.utils.Result

interface ProductsRepository {

    suspend fun getAllProducts(): Result<List<Product>, Throwable>

    suspend fun searchProduct(searchQuery: String): Result<List<Product>, Throwable>
}
