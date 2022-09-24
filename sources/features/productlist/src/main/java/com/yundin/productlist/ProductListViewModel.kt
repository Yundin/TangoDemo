package com.yundin.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yundin.core.model.Product
import com.yundin.core.repository.ProductsRepository
import com.yundin.core.utils.NativeText
import com.yundin.core.utils.Result
import com.yundin.core.utils.RetryTrigger
import com.yundin.core.utils.retryable
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
internal class ProductListViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState
    private val productsRetryTrigger = RetryTrigger()

    init {
        observeSearchInput()
    }

    fun onInputChange(text: String) {
        _uiState.value = _uiState.value.copy(searchText = text)
    }

    fun onRetryClick() {
        productsRetryTrigger.retry()
    }

    private fun observeSearchInput() {
        viewModelScope.launch {
            uiState.map { it.searchText }
                .distinctUntilChanged()
                .debounce(SEARCH_DEBOUNCE_DELAY)
                .flatMapLatest { searchQuery ->
                    handleNewSearchInput(searchQuery)
                }
                .retryable(productsRetryTrigger)
                .collect {
                    _uiState.value = it
                }
        }
    }

    private fun handleNewSearchInput(searchText: String): Flow<UiState> {
        return flow {
            emit(getProducts(searchText))
        }
            .map { productsResult ->
                when(productsResult) {
                    is Result.Success -> uiState.value.loaded(productsResult.result)
                    is Result.Error -> uiState.value.withError(
                        NativeText.Resource(R.string.loading_error)
                    )
                }
            }
            .onStart {
                emit(uiState.value.loading())
            }
    }

    private suspend fun getProducts(searchText: String): Result<List<Product>, Throwable> {
        return if (searchText.isBlank()) {
            productsRepository.getAllProducts()
        } else {
            productsRepository.searchProduct(searchText)
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 300L
    }
}

internal data class UiState(
    val searchText: String = "",
    val products: List<Product> = listOf(),
    val loadingState: LoadingState = LoadingState.Idle
)

internal sealed class LoadingState {
    object Idle : LoadingState()
    object Loading : LoadingState()
    data class Error(val message: NativeText) : LoadingState()
}

private fun UiState.loading(): UiState {
    return copy(loadingState = LoadingState.Loading)
}

private fun UiState.loaded(products: List<Product>): UiState {
    return copy(
        products = products,
        loadingState = LoadingState.Idle
    )
}

private fun UiState.withError(message: NativeText): UiState {
    return copy(
        loadingState = LoadingState.Error(message)
    )
}
