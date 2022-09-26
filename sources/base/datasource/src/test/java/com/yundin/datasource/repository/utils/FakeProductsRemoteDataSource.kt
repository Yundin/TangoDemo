package com.yundin.datasource.repository.utils

import com.yundin.core.utils.Result
import com.yundin.datasource.datasource.ProductsRemoteDataSource
import com.yundin.datasource.model.ServerProduct

class FakeProductsRemoteDataSource : ProductsRemoteDataSource {
    private var result: Result<List<ServerProduct>, Throwable> = Result.Success(emptyList())
    private var search: suspend (String) -> Result<List<ServerProduct>, Throwable> = { result }

    fun setResult(serverProducts: List<ServerProduct>) {
        result = Result.Success(serverProducts)
    }

    fun setErrorResult() {
        result = Result.Error(Throwable())
    }

    fun setSearchFunction(search: suspend (String) -> Result<List<ServerProduct>, Throwable>) {
        this.search = search
    }

    override suspend fun getAllProducts(): Result<List<ServerProduct>, Throwable> {
        return result
    }

    override suspend fun searchProduct(searchQuery: String): Result<List<ServerProduct>, Throwable> {
        return search(searchQuery)
    }
}