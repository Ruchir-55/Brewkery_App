package com.ruchir55.brewkery.state

import com.ruchir55.brewkery.model.ActiveOrder
import kotlin.random.Random

object OrderManager {

    private var activeOrder: ActiveOrder? = null

    fun placeOrder(
        itemCount: Int
    ): ActiveOrder {

        val ticketNumber =
            Random.nextInt(
                from = 10000,
                until = 100000
            )

        val ticket =
            "#BK-$ticketNumber"

        val order =
            ActiveOrder(
                orderId = ticket,
                itemCount = itemCount,
                waitTime = "25 mins"
            )

        activeOrder = order

        return order
    }

    fun getActiveOrder(): ActiveOrder? {
        return activeOrder
    }
}