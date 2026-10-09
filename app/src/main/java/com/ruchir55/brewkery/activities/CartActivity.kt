package com.ruchir55.brewkery.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ruchir55.brewkery.activities.MainActivity
import com.ruchir55.brewkery.adapters.CartAdapter
import com.ruchir55.brewkery.databinding.ActivityCartBinding
import com.ruchir55.brewkery.state.CartManager
import com.ruchir55.brewkery.state.OrderManager
import com.ruchir55.brewkery.utils.PriceCalculator
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    private lateinit var adapter: CartAdapter

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityCartBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()

        refreshCart()
    }

    private fun setupRecyclerView() {

        adapter =
            CartAdapter(
                CartManager.getItems()
            ) { position, amount ->

                CartManager.changeQuantity(
                    position,
                    amount
                )

                refreshCart()
            }

        binding.cartRecyclerView.layoutManager =
            LinearLayoutManager(this)

        binding.cartRecyclerView.adapter =
            adapter
    }

    private fun setupListeners() {

        binding.backButton.setOnClickListener {

            finish()
        }

        binding.clearCartButton.setOnClickListener {

            CartManager.clear()

            refreshCart()
        }

        binding.placeOrderButton.setOnClickListener {

            placeOrder()
        }
    }

    private fun refreshCart() {

        val items =
            CartManager.getItems()

        adapter.updateItems(
            items
        )

        val empty =
            items.isEmpty()

        binding.emptyText.visibility =
            if (empty)
                View.VISIBLE
            else
                View.GONE

        binding.cartRecyclerView.visibility =
            if (empty)
                View.GONE
            else
                View.VISIBLE

        val subtotal =
            CartManager.subtotal()

        val totals =
            PriceCalculator.calculateCartTotals(
                subtotal =
                    subtotal,
                deliveryFee =
                    CartManager.getDeliveryFee(),
                taxRatePercent =
                    CartManager.getTaxRate()
            )

        binding.subtotalText.text =
            String.format(
                Locale.US,
                "$%.2f",
                totals.subtotal
            )

        binding.deliveryText.text =
            String.format(
                Locale.US,
                "$%.2f",
                totals.deliveryFee
            )

        binding.taxText.text =
            String.format(
                Locale.US,
                "$%.2f",
                totals.tax
            )

        binding.totalText.text =
            String.format(
                Locale.US,
                "$%.2f",
                totals.total
            )

        binding.placeOrderButton.text =
            String.format(
                Locale.US,
                "Place Order Now  •  $%.2f",
                totals.total
            )

        binding.placeOrderButton.isEnabled =
            !empty
    }

    private fun placeOrder() {

        if (CartManager.isEmpty()) {
            return
        }

        val itemCount =
            CartManager.itemCount()

        OrderManager.placeOrder(
            itemCount
        )

        CartManager.clear()

        startActivity(
            Intent(
                this,
                OrderStatusActivity::class.java
            )
        )

        finish()
    }
}