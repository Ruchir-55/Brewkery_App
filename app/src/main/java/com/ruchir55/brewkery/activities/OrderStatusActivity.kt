package com.ruchir55.brewkery.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ruchir55.brewkery.activities.MainActivity
import com.ruchir55.brewkery.databinding.ActivityOrderStatusBinding
import com.ruchir55.brewkery.state.OrderManager

class OrderStatusActivity : AppCompatActivity() {

    private lateinit var binding:
            ActivityOrderStatusBinding

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityOrderStatusBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        val order =
            OrderManager.getActiveOrder()

        if (order != null) {

            binding.orderCode.text =
                order.orderId

            binding.waitText.text =
                "20 - 30 minutes"

            binding.itemsText.text =
                "${order.itemCount} Item(s)"

        } else {

            binding.orderCode.text =
                "No active order"

            binding.waitText.text =
                "-"

            binding.itemsText.text =
                "0 Items"
        }

        binding.backToMenuButton.setOnClickListener {

            val intent =
                Intent(
                    this,
                    MainActivity::class.java
                )

            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP

            startActivity(intent)

            finish()
        }
    }
}