package com.yundin.productlist

import com.yundin.core.repository.ProductsRepository
import com.yundin.productlist.utils.FakeProductsRepository
import com.yundin.productlist.utils.createProduct
import com.yundin.productlist.utils.viewModelTestingRules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModelTest {
    @get:Rule
    val viewModelRule = viewModelTestingRules()

    @Test
    fun `products list loads on start`() = runTest {
        val products = listOf(
            createProduct(name = "1"),
            createProduct(name = "2"),
            createProduct(name = "3"),
        )
        val repository = FakeProductsRepository().apply {
            setResult(products)
        }

        val viewModel = createProductListViewModel(repository)
        val uiState = viewModel.uiState.value

        val desiredState = UiState(
            searchText = "",
            products = products,
            loadingState = LoadingState.Idle
        )
        assertEquals(
            desiredState,
            uiState
        )
    }

    private fun createProductListViewModel(repository: ProductsRepository): ProductListViewModel =
        ProductListViewModel(repository)
}