package com.yundin.datasource.repository

import com.yundin.core.model.Product
import com.yundin.core.utils.Result
import com.yundin.datasource.datasource.ProductsRemoteDataSource
import com.yundin.datasource.model.toDomain
import com.yundin.datasource.repository.utils.FakeProductsRemoteDataSource
import com.yundin.datasource.repository.utils.createServerManufacturer
import com.yundin.datasource.repository.utils.createServerProduct
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal


@OptIn(ExperimentalCoroutinesApi::class)
class ProductsRepositoryImplTest {

    @Test
    fun `returns mapped products`() = runTest {
        val serverProduct = createServerProduct(
            id = "0",
            productName = "name",
            grossPrice = "0",
            manufacturer = createServerManufacturer(name = "manufacturer")
        )
        val serverProducts = listOf(serverProduct)
        val dataSource = FakeProductsRemoteDataSource().apply {
            setResult(serverProducts)
        }
        val repository = createProductsRepository(dataSource)

        val result = repository.getAllProducts()

        val expected = Result.Success(
            listOf(
                Product(
                    name = serverProduct.productName,
                    imageUrl = serverProduct.images.first().imageUrl,
                    manufacturerName = serverProduct.manufacturer.name,
                    price = BigDecimal.ZERO
                )
            )
        )
        assertEquals(expected, result)
    }

    @Test
    fun `returns error if failure`() = runTest {
        val dataSource = FakeProductsRemoteDataSource().apply {
            setErrorResult()
        }
        val repository = createProductsRepository(dataSource)

        val result = repository.getAllProducts()

        assert(result is Result.Error) {
            "we should get error if data source returns error"
        }
    }

    @Test
    fun `returns mapped products on search`() = runTest {
        val serverProducts = listOf(
            createServerProduct(id = "0")
        )
        val searchText = "search"
        val dataSource = FakeProductsRemoteDataSource().apply {
            setSearchFunction { searchRequest ->
                if (searchRequest == searchText) {
                    Result.Success(serverProducts)
                } else {
                    Result.Success(listOf())
                }
            }
        }
        val repository = createProductsRepository(dataSource)

        val result = repository.searchProduct(searchText)

        val expected = Result.Success(
            serverProducts.map { it.toDomain() }
        )
        assertEquals(expected, result)
    }

    @Test
    fun `returns error on search request if failure`() = runTest {
        val dataSource = FakeProductsRemoteDataSource().apply {
            setErrorResult()
        }
        val repository = createProductsRepository(dataSource)

        val result = repository.searchProduct("")

        assert(result is Result.Error) {
            "we should get error if data source returns error"
        }
    }

    private fun createProductsRepository(
        dataSource: ProductsRemoteDataSource
    ): ProductsRepositoryImpl = ProductsRepositoryImpl(dataSource)
}