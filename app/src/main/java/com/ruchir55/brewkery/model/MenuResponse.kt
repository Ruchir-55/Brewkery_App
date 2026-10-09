package com.ruchir55.brewkery.model

data class MenuResponse(
    val meta: StoreMeta,
    val categories: List<Category>,
    val items: List<MenuItem>
)

data class StoreMeta(
    val app: String,
    val version: String,
    val tagline: String,
    val currency: String,
    val currency_symbol: String,
    val delivery_fee: Double,
    val tax_rate_percent: Double,
    val estimated_delivery_time: String
)

data class Category(
    val id: String,
    val name: String,
    val icon: String,
    val item_count: Int
)