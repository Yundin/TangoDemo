package com.yundin.datasource.repository

import com.yundin.core.model.Product
import com.yundin.core.repository.ProductsRepository
import com.yundin.core.utils.Result
import com.yundin.core.utils.mapSuccess
import com.yundin.datasource.datasource.ProductsRemoteDataSource
import com.yundin.datasource.model.toDomain
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val remoteDataSource: ProductsRemoteDataSource
) : ProductsRepository {

    override suspend fun getAllProducts(): Result<List<Product>, Throwable> {
        return remoteDataSource.getAllProducts()
            .mapSuccess { list -> list.map { it.toDomain() } }
    }

    override suspend fun searchProduct(searchQuery: String): Result<List<Product>, Throwable> {
        return remoteDataSource.searchProduct(searchQuery)
            .mapSuccess { list -> list.map { it.toDomain() } }
    }
}
