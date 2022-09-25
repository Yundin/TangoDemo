package com.yundin.datasource.datasource

import android.util.Log
import com.yundin.core.utils.Result
import com.yundin.core.utils.doOnError
import com.yundin.core.utils.runBlockCatching
import com.yundin.datasource.model.ServerProduct
import com.yundin.datasource.network.TangoDemoApi
import javax.inject.Inject

interface ProductsRemoteDataSource {

    suspend fun getAllProducts(): Result<List<ServerProduct>, Throwable>

    suspend fun searchProduct(searchQuery: String): Result<List<ServerProduct>, Throwable>
}

class ProductsRemoteDataSourceImpl @Inject constructor(
    private val api: TangoDemoApi
): ProductsRemoteDataSource {

    override suspend fun getAllProducts(): Result<List<ServerProduct>, Throwable> {
        return runBlockCatching { api.getProducts() }
            .doOnError { Log.e("::getAllProducts server", it.stackTraceToString()) }

    }

    override suspend fun searchProduct(searchQuery: String): Result<List<ServerProduct>, Throwable> {
        return runBlockCatching { api.searchProducts(searchQuery) }
            .doOnError { Log.e("::searchProduct server", it.stackTraceToString()) }
    }
}
