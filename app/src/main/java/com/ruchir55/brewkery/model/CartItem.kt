package com.ruchir55.brewkery.model

data class CartItem(
    val id: Int,
    val name: String,
    val size: String,
    val milk: String,
    val sugar: String,
    val unitPrice: Double,
    var quantity: Int
) {
    val totalPrice: Double
        get() = unitPrice * quantity
}