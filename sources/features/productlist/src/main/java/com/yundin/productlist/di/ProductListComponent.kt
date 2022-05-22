package com.yundin.productlist.di

import com.yundin.core.ProductListDependencies
import com.yundin.core.dagger.FeatureViewModelBuilder
import com.yundin.core.dagger.FeatureViewModelComponent
import com.yundin.core.dagger.scope.FeatureScope
import com.yundin.productlist.ProductListViewModel
import dagger.Component

@[FeatureScope Component(dependencies = [ProductListDependencies::class])]
internal interface ProductListComponent : FeatureViewModelComponent<ProductListViewModel> {

    @Component.Builder
    interface Builder : FeatureViewModelBuilder<ProductListDependencies, ProductListViewModel>
}
