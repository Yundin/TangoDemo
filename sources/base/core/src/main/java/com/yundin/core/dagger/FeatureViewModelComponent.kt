package com.yundin.core.dagger

interface FeatureViewModelComponent<VM> {
    val viewModel: VM
}

interface FeatureViewModelBuilder<in DEPS, VM> {
    fun dependencies(dependencies: DEPS): FeatureViewModelBuilder<DEPS, VM>
    fun build(): FeatureViewModelComponent<VM>
}
