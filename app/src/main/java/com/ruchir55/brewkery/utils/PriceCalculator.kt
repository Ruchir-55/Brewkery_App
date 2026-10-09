package com.ruchir55.brewkery.utils

data class CartTotals(
    val subtotal: Double,
    val deliveryFee: Double,
    val tax: Double,
    val total: Double
)

object PriceCalculator {

    fun calculateCartTotals(
        subtotal: Double,
        deliveryFee: Double,
        taxRatePercent: Double
    ): CartTotals {

        if (subtotal <= 0.0) {

            return CartTotals(
                subtotal = 0.0,
                deliveryFee = 0.0,
                tax = 0.0,
                total = 0.0
            )
        }

        val tax =
            subtotal * (taxRatePercent / 100.0)

        val total =
            subtotal + deliveryFee + tax

        return CartTotals(
            subtotal = subtotal,
            deliveryFee = deliveryFee,
            tax = tax,
            total = total
        )
    }
}