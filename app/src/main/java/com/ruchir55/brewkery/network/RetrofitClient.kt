package com.ruchir55.brewkery.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL =
        "https://raw.githubusercontent.com/VivekShah138/Brewkery/main/"

    val api: BrewkeryApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(BrewkeryApiService::class.java)
    }
}