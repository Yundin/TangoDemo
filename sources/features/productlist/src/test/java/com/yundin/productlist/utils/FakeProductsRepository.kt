package com.yundin.productlist.utils

import com.yundin.core.model.Product
import com.yundin.core.repository.ProductsRepository
import com.yundin.core.utils.Result

internal class FakeProductsRepository : ProductsRepository {
    private var result: Result<List<Product>, Throwable> = Result.Success(emptyList())
    private var search: suspend (String) -> Result<List<Product>, Throwable> = { result }

    fun setResult(movies: List<Product>) {
        result = Result.Success(movies)
    }

    fun setErrorResult() {
        result = Result.Error(Throwable())
    }

    fun setSearchFunction(search: suspend (String) -> Result<List<Product>, Throwable>) {
        this.search = search
    }

    override suspend fun getAllProducts(): Result<List<Product>, Throwable> {
        return result
    }

    override suspend fun searchProduct(searchQuery: String): Result<List<Product>, Throwable> {
        return search(searchQuery)
    }
}