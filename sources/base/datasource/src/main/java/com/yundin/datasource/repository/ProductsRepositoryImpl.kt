package com.yundin.datasource.repository

import com.yundin.core.model.Product
import com.yundin.core.repository.ProductsRepository
import com.yundin.core.utils.Result
import com.yundin.core.utils.runBlockCatching
import com.yundin.datasource.TangoDemoApi
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val api: TangoDemoApi
) : ProductsRepository {

    override suspend fun getAllProducts(): Result<List<Product>, Throwable> {
        return runBlockCatching {
            api.getProducts().map { it.toDomain() }
        }
    }

    override suspend fun searchProduct(searchQuery: String): Result<List<Product>, Throwable> {
        return runBlockCatching {
            api.searchProducts(searchQuery).map { it.toDomain() }
        }
    }
}
