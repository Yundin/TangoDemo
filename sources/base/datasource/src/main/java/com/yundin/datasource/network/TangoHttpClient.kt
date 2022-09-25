package com.yundin.datasource.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface TangoHttpClient {

    val tangoApi: TangoDemoApi
}

class TangoHttpClientImpl @Inject constructor() : TangoHttpClient {

    companion object {
        const val NETWORK_TIMEOUT_SECONDS = 100L
    }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .readTimeout(NETWORK_TIMEOUT_SECONDS, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://radiant-inlet-94783.herokuapp.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    override val tangoApi: TangoDemoApi by lazy { retrofit.create(TangoDemoApi::class.java) }
}
