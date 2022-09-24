package com.yundin.productlist

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yundin.core.App
import com.yundin.core.utils.daggerViewModelFactory
import com.yundin.designsystem.LoadingFailedItem
import com.yundin.designsystem.ProductItem
import com.yundin.designsystem.ProgressBarItem
import com.yundin.designsystem.SearchField
import com.yundin.productlist.di.DaggerProductListComponent

@Composable
fun ProductListScreen() {
    val app = LocalContext.current.applicationContext as App
    val viewModel: ProductListViewModel = viewModel(
        factory = daggerViewModelFactory(
            dependencies = app.getAppProvider(),
            builder = DaggerProductListComponent.builder()
        )
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ProductListScreenContent(
        uiState = uiState,
        onSearchTextChanged = viewModel::onInputChange,
        onRetryClick = viewModel::onRetryClick
    )
}

@Composable
private fun ProductListScreenContent(
    uiState: UiState,
    onSearchTextChanged: (String) -> Unit,
    onRetryClick: () -> Unit
) {
    Column {
        SearchField(
            value = uiState.searchText,
            onValueChange = onSearchTextChanged,
            label = stringResource(R.string.product_search_field_hint)
        )
        Divider()
        ProductList(uiState = uiState, onRetryClick = onRetryClick)
        EmptyState(uiState = uiState)
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun LoadingStateItem(
    state: LoadingState,
    onRetryClick: () -> Unit
) {
    AnimatedContent(targetState = state) {
        if (it is LoadingState.Loading) {
            ProgressBarItem()
        }
        if (it is LoadingState.Error) {
            LoadingFailedItem(onRetryClick = onRetryClick)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProductList(
    uiState: UiState,
    onRetryClick: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    LaunchedEffect(uiState.loadingState) {
        // new state, scroll to top
        lazyListState.animateScrollToItem(0, 0)
    }
    LazyVerticalGrid(
        state = lazyListState,
        modifier = Modifier.fillMaxWidth(),
        cells = GridCells.Fixed(2)
    ) {
        item(
            span = { GridItemSpan(2) }
        ) {
            LoadingStateItem(
                state = uiState.loadingState,
                onRetryClick = onRetryClick
            )
        }
        items(uiState.products) {
            ProductItem(
                name = it.name,
                imageUrl = it.imageUrl,
                manufacturer = it.manufacturerName,
                price = it.price
            )
        }
    }
}

@Composable
private fun EmptyState(uiState: UiState) {
    if (uiState.loadingState == LoadingState.Idle && uiState.products.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.product_search_empty_message),
            )
        }
    }
}

