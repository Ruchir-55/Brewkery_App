package com.ruchir55.brewkery

import com.ruchir55.brewkery.utils.PriceCalculator
import org.junit.Assert.assertEquals
import org.junit.Test

class PriceCalculatorTest {

    @Test
    fun calculateCartTotalCorrectly() {

        val result =
            PriceCalculator.calculateCartTotals(
                subtotal = 9.40,
                deliveryFee = 2.50,
                taxRatePercent = 8.0
            )

        assertEquals(
            9.40,
            result.subtotal,
            0.001
        )

        assertEquals(
            2.50,
            result.deliveryFee,
            0.001
        )

        assertEquals(
            0.752,
            result.tax,
            0.001
        )

        assertEquals(
            12.652,
            result.total,
            0.001
        )
    }
}