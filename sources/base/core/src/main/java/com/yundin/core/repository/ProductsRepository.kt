package com.yundin.core.repository

import com.yundin.core.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductsRepository {

    val products: Flow<List<Product>>

    fun searchProduct(searchQuery: String): Flow<List<Product>>
}