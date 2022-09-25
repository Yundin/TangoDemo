package com.yundin.productlist.utils

import com.yundin.core.model.Product
import com.yundin.core.repository.ProductsRepository
import com.yundin.core.utils.Result

internal class FakeProductsRepository : ProductsRepository {
    private var result: Result<List<Product>, Throwable> = Result.Success(emptyList())

    fun setResult(movies: List<Product>) {
        result = Result.Success(movies)
    }

    fun setErrorResult() {
        result = Result.Error(Throwable())
    }

    override suspend fun getAllProducts(): Result<List<Product>, Throwable> {
        return result
    }

    override suspend fun searchProduct(searchQuery: String): Result<List<Product>, Throwable> {
        return result
    }
}