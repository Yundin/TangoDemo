package com.yundin.core.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yundin.core.dagger.FeatureViewModelBuilder

private class ViewModelProviderFactory(
    private val createViewModel: () -> ViewModel
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return createViewModel() as T
    }
}

fun daggerViewModelFactory(createViewModel: () -> ViewModel): ViewModelProvider.Factory {
    return ViewModelProviderFactory(createViewModel)
}

fun <DEPS, VM : ViewModel> daggerViewModelFactory(
    dependencies: DEPS,
    builder: FeatureViewModelBuilder<DEPS, VM>
): ViewModelProvider.Factory {
    return daggerViewModelFactory {
        builder.dependencies(dependencies).build().viewModel
    }
}
