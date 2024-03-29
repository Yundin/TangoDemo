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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
internal class ProductListViewModel @Inject constructor(
    private val productsRepository: ProductsRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState
    private val productsRetryTrigger = RetryTrigger()
    private var firstLoadingPassed = false

    init {
        observeSearchInput()
    }

    fun onInputChange(text: String) {
        _uiState.value = _uiState.value.copy(searchText = text)
    }

    fun onRetryClick() {
        firstLoadingPassed = false
        productsRetryTrigger.retry()
    }

    private fun observeSearchInput() {
        viewModelScope.launch {
            uiState.map { it.searchText }
                .distinctUntilChanged()
                .debounce {
                    if (firstLoadingPassed) {
                        SEARCH_DEBOUNCE_DELAY
                    } else {
                        firstLoadingPassed = true
                        0L
                    }
                }
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
            val productsResult = getProducts(searchText)
            val uiState = getUiForProductsResult(productsResult)
            emit(uiState)
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

    private fun getUiForProductsResult(result: Result<List<Product>, Throwable>): UiState {
        return when(result) {
            is Result.Success -> uiState.value.loaded(result.result)
            is Result.Error -> uiState.value.withError(
                NativeText.Resource(R.string.loading_error)
            )
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 300L
    }
}
