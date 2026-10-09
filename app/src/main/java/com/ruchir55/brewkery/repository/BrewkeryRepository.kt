package com.ruchir55.brewkery.repository

import com.ruchir55.brewkery.model.MenuItem
import com.ruchir55.brewkery.model.MenuResponse
import com.ruchir55.brewkery.network.BrewkeryApiService

class BrewkeryRepository(
    private val api: BrewkeryApiService
) {

    suspend fun getMenu(): MenuResponse {
        return api.getMenu()
    }

    suspend fun getItem(id: Int): MenuItem {
        return api.getItem(id)
    }
}