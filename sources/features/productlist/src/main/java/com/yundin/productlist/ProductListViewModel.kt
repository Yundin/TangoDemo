package com.yundin.productlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yundin.core.model.Product
import com.yundin.core.repository.ProductsRepository
import com.yundin.core.utils.ResourceProvider
import com.yundin.core.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
internal class ProductListViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState
    private var productsJob: Job? = null

    init {
        observeSearchInput()
    }

    fun onInputChange(text: String) {
        _uiState.value = _uiState.value.copy(searchText = text)
    }

    fun onRetryClick() {
        observeSearchInput()
    }

    private fun observeSearchInput() {
        productsJob?.cancel()
        productsJob = viewModelScope.launch {
            uiState.map { it.searchText }
                .distinctUntilChanged()
                .debounce(SEARCH_DEBOUNCE_DELAY)
                .flatMapLatest { searchQuery ->
                    proceedSearchInput(searchQuery)
                }
                .collect {
                    _uiState.value = it
                }
        }
    }

    private fun proceedSearchInput(searchText: String): Flow<UiState> {
        return flow {
            if (searchText.isBlank()) {
                emit(productsRepository.getAllProducts())
            } else {
                emit(productsRepository.searchProduct(searchText))
            }
        }
            .map { products ->
                when(products) {
                    is Result.Success -> uiState.value.loaded(products.result)
                    is Result.Error -> uiState.value.withError(
                        resourceProvider.getString(R.string.loading_error)
                    )
                }
            }
            .onStart {
                emit(
                    uiState.value.loading()
                )
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
    data class Error(val message: String) : LoadingState()
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

private fun UiState.withError(message: String): UiState {
    return copy(
        loadingState = LoadingState.Error(message)
    )
}
