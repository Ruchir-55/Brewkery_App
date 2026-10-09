package com.ruchir55.brewkery.state

import com.ruchir55.brewkery.model.CartItem

object CartManager {

    private val items =
        mutableListOf<CartItem>()

    private var deliveryFee = 2.50

    private var taxRatePercent = 8.0

    /*
     * The prototype starts with two demo cart items.
     * This gives the Android version the same initial
     * visual state as the supplied prototype.
     */
    init {

        items.add(
            CartItem(
                id = 1,
                name = "Toasted Caramel Macchiato",
                size = "Grande (12 oz)",
                milk = "Oat Milk (Barista Blend)",
                sugar = "50% Mild",
                unitPrice = 5.50,
                quantity = 1
            )
        )

        items.add(
            CartItem(
                id = 3,
                name = "Golden Normandy Croissant",
                size = "Single Piece",
                milk = "Classic (No Spread)",
                sugar = "Warm & Crisp (Recommended)",
                unitPrice = 3.90,
                quantity = 1
            )
        )
    }

    fun configureStore(
        deliveryFee: Double,
        taxRatePercent: Double
    ) {

        this.deliveryFee = deliveryFee
        this.taxRatePercent = taxRatePercent
    }

    fun add(item: CartItem) {
        items.add(item)
    }

    fun getItems(): List<CartItem> {
        return items.toList()
    }

    fun changeQuantity(
        position: Int,
        amount: Int
    ) {

        if (position !in items.indices) {
            return
        }

        items[position].quantity += amount

        if (items[position].quantity <= 0) {
            items.removeAt(position)
        }
    }

    fun clear() {
        items.clear()
    }

    fun isEmpty(): Boolean {
        return items.isEmpty()
    }

    fun itemCount(): Int {

        return items.sumOf {
            it.quantity
        }
    }

    fun subtotal(): Double {

        return items.sumOf {
            it.totalPrice
        }
    }

    fun getDeliveryFee(): Double {
        return deliveryFee
    }

    fun getTaxRate(): Double {
        return taxRatePercent
    }
}