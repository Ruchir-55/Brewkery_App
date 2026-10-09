package com.ruchir55.brewkery.network

import com.ruchir55.brewkery.model.MenuItem
import com.ruchir55.brewkery.model.MenuResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface BrewkeryApiService {

    @GET("data.json")
    suspend fun getMenu(): MenuResponse

    @GET("api/items/{id}.json")
    suspend fun getItem(
        @Path("id") id: Int
    ): MenuItem
}