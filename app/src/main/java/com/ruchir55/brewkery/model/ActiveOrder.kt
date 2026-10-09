package com.ruchir55.brewkery.model

data class ActiveOrder(
    val orderId: String,
    val itemCount: Int,
    val waitTime: String
)