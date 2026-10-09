package com.ruchir55.brewkery.model

data class MenuItem(
    val id: Int,
    val category_id: String,
    val name: String,
    val tagline: String,
    val description: String,
    val base_price: Double,
    val rating: Double,
    val review_count: Int,
    val prep_time: String,
    val calories: Int,
    val image_url: String,
    val badge: String,
    val ingredients: List<String>,
    val customizations: Customizations
)

data class Customizations(
    val sizes: List<SizeOption>,
    val sugar_levels: List<String>,
    val milk_options: List<MilkOption>
)

data class SizeOption(
    val id: String,
    val label: String,
    val extra_price: Double
)

data class MilkOption(
    val id: String,
    val name: String,
    val extra_price: Double
)