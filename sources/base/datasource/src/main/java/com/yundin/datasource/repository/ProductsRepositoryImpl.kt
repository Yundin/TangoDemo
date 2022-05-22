package com.yundin.datasource.repository

import com.yundin.core.model.Product
import com.yundin.core.repository.ProductsRepository
import com.yundin.datasource.TangoDemoApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductsRepositoryImpl @Inject constructor(
    private val api: TangoDemoApi
) : ProductsRepository {

    override val products: Flow<List<Product>>
        get() = flow {
            emit(api.getProducts().map { it.toDomain() })
        }

    override fun searchProduct(searchQuery: String): Flow<List<Product>> {
        return flow {
            emit(api.searchProducts(searchQuery).map { it.toDomain() })
        }
    }
}
