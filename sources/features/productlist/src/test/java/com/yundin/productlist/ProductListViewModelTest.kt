package com.yundin.productlist

import com.yundin.core.repository.ProductsRepository
import com.yundin.core.utils.NativeText
import com.yundin.core.utils.Result
import com.yundin.productlist.utils.FakeProductsRepository
import com.yundin.productlist.utils.createProduct
import com.yundin.productlist.utils.viewModelTestingRules
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
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

        val expectedState = UiState(
            searchText = "",
            products = products,
            loadingState = LoadingState.Idle
        )
        assertEquals(
            expectedState,
            uiState
        )
    }

    @Test
    fun `search text is updated on demand`() = runTest {
        val repository = FakeProductsRepository()
        val viewModel = createProductListViewModel(repository)
        val newSearchText = "Some text"

        viewModel.onInputChange(newSearchText)
        val uiState = viewModel.uiState.value

        assertEquals(newSearchText, uiState.searchText)
    }

    @Test
    fun `after search text updated search result is displayed`() = runTest {
        val productsForSearch = listOf(
            createProduct(name = "1"),
        )
        val searchText = "Search text"
        val repository = FakeProductsRepository().apply {
            setResult(listOf())
            setSearchFunction { searchRequest ->
                val products = if (searchRequest == searchText) {
                    productsForSearch
                } else {
                    listOf()
                }
                Result.Success(products)
            }
        }
        val viewModel = createProductListViewModel(repository)

        viewModel.onInputChange(searchText)
        waitForDebounce()
        val uiState = viewModel.uiState.value

        val expectedState = UiState(
            searchText = searchText,
            products = productsForSearch,
            loadingState = LoadingState.Idle
        )
        assertEquals(expectedState, uiState)
    }

    @Test
    fun `if search text is empty all products are displayed`() = runTest {
        val products = listOf(
            createProduct(name = "1"),
        )
        val repository = FakeProductsRepository().apply {
            setResult(products)
            setSearchFunction { Result.Success(listOf()) }
        }
        val viewModel = createProductListViewModel(repository)

        viewModel.onInputChange("search")
        viewModel.onInputChange("")
        waitForDebounce()
        val uiState = viewModel.uiState.value

        assertEquals(products, uiState.products)
    }

    @Test
    fun `error state is displayed`() = runTest {
        val repository = FakeProductsRepository().apply {
            setErrorResult()
        }
        val viewModel = createProductListViewModel(repository)

        val uiState = viewModel.uiState.value

        assertEquals(
            LoadingState.Error(NativeText.Resource(R.string.loading_error)),
            uiState.loadingState
        )
    }

    @Test
    fun `previous products are displayed when loading or error`() = runTest {
        val products = listOf(
            createProduct(name = "1")
        )
        val repository = FakeProductsRepository().apply {
            setResult(products)
        }
        val viewModel = createProductListViewModel(repository)
        val searchText = "some text"

        repository.setSearchFunction {
            delay(100)
            Result.Error(Throwable())
        }
        viewModel.onInputChange(searchText)
        waitForDebounce()
        var uiState = viewModel.uiState.value
        val expectedLoadingState = UiState(
            searchText = searchText,
            products = products,
            loadingState = LoadingState.Loading
        )

        assertEquals(uiState, expectedLoadingState)

        advanceTimeBy(101) // wait for search result

        val expectedErrorState = UiState(
            searchText = searchText,
            products = products,
            loadingState = LoadingState.Error(NativeText.Resource(R.string.loading_error))
        )
        uiState = viewModel.uiState.value

        assertEquals(expectedErrorState, uiState)
    }

    @Test
    fun `retry call retries data request`() = runTest {
        val repository = FakeProductsRepository().apply {
            setErrorResult()
        }
        val viewModel = createProductListViewModel(repository)
        val products = listOf(
            createProduct(name = "1")
        )

        repository.setResult(products)
        viewModel.onRetryClick()
        val uiState = viewModel.uiState.value

        assertEquals(products, uiState.products)
    }

    private fun createProductListViewModel(repository: ProductsRepository): ProductListViewModel =
        ProductListViewModel(repository)

    private fun TestScope.waitForDebounce() {
        advanceTimeBy(301)
    }
}
