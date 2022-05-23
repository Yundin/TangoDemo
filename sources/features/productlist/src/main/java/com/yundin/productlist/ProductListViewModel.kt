package com.yundin.productlist

import androidx.lifecycle.*
import com.yundin.core.model.Product
import com.yundin.core.repository.ProductsRepository
import com.yundin.core.utils.ResourceProvider
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
    private val _uiState: MutableLiveData<UiState> = MutableLiveData(UiState())
    val uiState: LiveData<UiState> = _uiState
    private var productsJob: Job? = null

    init {
        observeSearchInput()
    }

    fun onInputChange(text: String) {
        _uiState.value = _uiState.value!!.copy(searchText = text)
    }

    fun onRetryClick() {
        observeSearchInput()
    }

    private fun observeSearchInput() {
        productsJob?.cancel()
        productsJob = viewModelScope.launch {
            uiState.asFlow()
                .map { it.searchText }
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
        val dataFlow =
            if (searchText.isBlank()) {
                productsRepository.products
            } else {
                productsRepository.searchProduct(searchText)
            }
        return dataFlow
            .map { products ->
                uiState.value!!.loaded(products)
            }
            .catch {
                emit(
                    uiState.value!!.withError(
                        resourceProvider.getString(R.string.loading_error)
                    )
                )
            }
            .onStart {
                emit(
                    uiState.value!!.loading()
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
