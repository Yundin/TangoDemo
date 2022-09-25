package com.yundin.datasource.di

import com.yundin.core.dagger.scope.AppScope
import com.yundin.core.repository.ProductsRepository
import com.yundin.datasource.datasource.ProductsRemoteDataSource
import com.yundin.datasource.datasource.ProductsRemoteDataSourceImpl
import com.yundin.datasource.network.TangoDemoApi
import com.yundin.datasource.network.TangoHttpClient
import com.yundin.datasource.network.TangoHttpClientImpl
import com.yundin.datasource.repository.ProductsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataSourceModule {

    @Binds
    @AppScope
    fun bindHttpClient(impl: TangoHttpClientImpl): TangoHttpClient

    @Binds
    fun bindProductRepository(impl: ProductsRepositoryImpl): ProductsRepository

    @Binds
    fun bindProductsRemoteDataSource(impl: ProductsRemoteDataSourceImpl): ProductsRemoteDataSource

    companion object {
        @Provides
        fun provideApi(client: TangoHttpClient): TangoDemoApi {
            return client.tangoApi
        }
    }
}
