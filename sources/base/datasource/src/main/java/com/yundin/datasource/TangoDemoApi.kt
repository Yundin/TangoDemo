package com.yundin.datasource

import com.yundin.datasource.model.ServerProduct
import retrofit2.http.GET
import retrofit2.http.Query

interface TangoDemoApi {

    @GET("products")
    suspend fun getProducts(): List<ServerProduct>

    @GET("products")
    suspend fun searchProducts(@Query("name") query: String): List<ServerProduct>
}
