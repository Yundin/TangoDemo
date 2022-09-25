package com.yundin.productlist

import com.yundin.core.model.Product
import com.yundin.core.utils.NativeText

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

internal fun UiState.loading(): UiState {
    return copy(loadingState = LoadingState.Loading)
}

internal fun UiState.loaded(products: List<Product>): UiState {
    return copy(
        products = products,
        loadingState = LoadingState.Idle
    )
}

internal fun UiState.withError(message: NativeText): UiState {
    return copy(
        loadingState = LoadingState.Error(message)
    )
}
