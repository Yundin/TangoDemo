package com.yundin.datasource.di

import com.yundin.core.dagger.scope.AppScope
import com.yundin.core.repository.ProductsRepository
import com.yundin.datasource.TangoDemoApi
import com.yundin.datasource.repository.ProductsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
interface DataSourceModule {

    @Binds
    fun bindProductRepository(impl: ProductsRepositoryImpl): ProductsRepository

    companion object {
        @AppScope
        @Provides
        fun provideApi(): TangoDemoApi {
            val client: OkHttpClient = OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://radiant-inlet-94783.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(TangoDemoApi::class.java)
        }
    }
}
