package com.ruchir55.brewkery.activities

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.button.MaterialButton
import com.ruchir55.brewkery.activities.CartActivity
import com.ruchir55.brewkery.activities.ItemDetailActivity
import com.ruchir55.brewkery.activities.OrderStatusActivity
import com.ruchir55.brewkery.adapters.MenuAdapter
import com.ruchir55.brewkery.databinding.ActivityMainBinding
import com.ruchir55.brewkery.model.Category
import com.ruchir55.brewkery.model.MenuItem
import com.ruchir55.brewkery.repository.BrewkeryRepository
import com.ruchir55.brewkery.network.RetrofitClient
import com.ruchir55.brewkery.state.CartManager
import com.ruchir55.brewkery.state.OrderManager
import kotlinx.coroutines.launch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: MenuAdapter

    private val repository =
        BrewkeryRepository(RetrofitClient.api)

    private var allItems =
        emptyList<MenuItem>()

    private var categories =
        emptyList<Category>()

    private var selectedCategory =
        "ALL"

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityMainBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupRecyclerView()
        setupListeners()
        loadMenu()
    }

    override fun onResume() {
        super.onResume()

        updateCartBar()
        updateActiveOrder()
    }

    private fun setupRecyclerView() {

        adapter =
            MenuAdapter(
                emptyList()
            ) { item ->

                val intent =
                    Intent(
                        this,
                        ItemDetailActivity::class.java
                    )

                intent.putExtra(
                    "itemId",
                    item.id
                )

                startActivity(intent)
            }

        binding.menuRecyclerView.layoutManager =
            LinearLayoutManager(this)

        binding.menuRecyclerView.adapter =
            adapter
    }

    private fun setupListeners() {

        binding.cartBar.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CartActivity::class.java
                )
            )
        }

        binding.cartButton.setOnClickListener {

            startActivity(
                Intent(
                    this,
                    CartActivity::class.java
                )
            )
        }

        binding.activeOrderBanner.setOnClickListener {

            if (
                OrderManager.getActiveOrder() != null
            ) {

                startActivity(
                    Intent(
                        this,
                        OrderStatusActivity::class.java
                    )
                )
            }
        }

        binding.searchEditText
            .addTextChangedListener {

                applyFilters()
            }
    }

    private fun loadMenu() {

        showLoading(true)

        lifecycleScope.launch {

            try {

                val response =
                    repository.getMenu()

                allItems =
                    response.items

                categories =
                    response.categories

                CartManager.configureStore(
                    response.meta.delivery_fee,
                    response.meta.tax_rate_percent
                )

                binding.bannerMain.text =
                    "Delivery in ${response.meta.estimated_delivery_time}"

                binding.bannerSub.text =
                    String.format(
                        Locale.US,
                        "$%.2f flat fee",
                        response.meta.delivery_fee
                    )

                createCategoryButtons()

                applyFilters()

                showLoading(false)

            } catch (e: Exception) {

                showLoading(false)

                binding.errorText.visibility =
                    View.VISIBLE

                binding.errorText.text =
                    "Unable to load menu. Check your internet connection and try again."

            }
        }
    }

    private fun createCategoryButtons() {

        binding.categoryContainer.removeAllViews()

        addCategoryButton(
            text = "All Items",
            categoryId = "ALL"
        )

        categories.forEach { category ->

            addCategoryButton(
                text = "${category.icon} ${category.name}",
                categoryId = category.id
            )
        }

        updateCategoryButtonStyles()
    }

    private fun addCategoryButton(
        text: String,
        categoryId: String
    ) {
        val button = MaterialButton(this)

        button.text = text
        button.isAllCaps = false
        button.textSize = 13f

        // Rounded pill-shaped buttons
        button.cornerRadius = dpToPx(24)

        // Consistent button dimensions
        button.minimumHeight = dpToPx(40)
        button.minHeight = dpToPx(40)
        button.minimumWidth = 0
        button.minWidth = 0

        // Internal spacing
        button.setPadding(
            dpToPx(14),
            0,
            dpToPx(14),
            0
        )

        // Remove MaterialButton's default extra insets
        button.insetTop = 0
        button.insetBottom = 0

        // Prevent long category names from wrapping
        button.maxLines = 1

        // Use wrap_content width and consistent spacing
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            dpToPx(40)
        ).apply {
            marginEnd = dpToPx(8)
        }

        button.layoutParams = params

        // Identify the category represented by this button
        button.tag = categoryId

        button.setOnClickListener {
            selectedCategory = categoryId

            updateCategoryButtonStyles()
            applyFilters()
        }

        binding.categoryContainer.addView(button)
    }

    private fun updateCategoryButtonStyles() {

        for (index in 0 until binding.categoryContainer.childCount) {

            val button = binding.categoryContainer
                .getChildAt(index) as MaterialButton

            val active = button.tag == selectedCategory

            if (active) {

                // Selected category
                button.backgroundTintList =
                    android.content.res.ColorStateList.valueOf(
                        Color.parseColor("#D9502B")
                    )

                button.setTextColor(Color.WHITE)

                button.strokeWidth = 0

            } else {

                // Unselected categories
                button.backgroundTintList =
                    android.content.res.ColorStateList.valueOf(
                        Color.WHITE
                    )

                button.setTextColor(
                    Color.parseColor("#786457")
                )

                button.strokeWidth = dpToPx(1)

                button.strokeColor =
                    android.content.res.ColorStateList.valueOf(
                        Color.parseColor("#EBD8CB")
                    )
            }
        }
    }

    private fun applyFilters() {

        val query =
            binding.searchEditText
                .text
                .toString()
                .trim()
                .lowercase(Locale.getDefault())

        var filtered =
            allItems

        if (
            selectedCategory != "ALL"
        ) {

            filtered =
                filtered.filter {
                    it.category_id ==
                            selectedCategory
                }
        }

        if (query.isNotEmpty()) {

            filtered =
                filtered.filter {

                    it.name
                        .lowercase(Locale.getDefault())
                        .contains(query)

                            ||

                            it.description
                                .lowercase(Locale.getDefault())
                                .contains(query)

                            ||

                            it.tagline
                                .lowercase(Locale.getDefault())
                                .contains(query)
                }
        }

        adapter.updateItems(
            filtered
        )
    }

    private fun showLoading(
        loading: Boolean
    ) {

        binding.progressBar.visibility =
            if (loading)
                View.VISIBLE
            else
                View.GONE

        if (loading) {

            binding.errorText.visibility =
                View.GONE
        }
    }

    private fun updateCartBar() {

        val count =
            CartManager.itemCount()

        val subtotal =
            CartManager.subtotal()

        binding.cartBadge.text =
            count.toString()

        binding.cartBarQty.text =
            count.toString()

        binding.cartBarSubtotal.text =
            String.format(
                Locale.US,
                "$%.2f",
                subtotal
            )

        binding.cartBar.visibility =
            if (count > 0)
                View.VISIBLE
            else
                View.GONE
    }

    private fun updateActiveOrder() {

        val order =
            OrderManager.getActiveOrder()

        if (order == null) {

            binding.activeOrderTag.text =
                "STORE INFO"

            binding.activeOrderIcon.text =
                "🛵"

            binding.bannerMain.text =
                "Delivery in 20 - 30 mins"

            binding.bannerSub.text =
                "$2.50 flat fee"

            binding.activeOrderAction.text =
                "Open"

        } else {

            binding.activeOrderTag.text =
                "ACTIVE ORDER"

            binding.activeOrderIcon.text =
                "🟢"

            binding.bannerMain.text =
                "Active Order ${order.orderId}"

            binding.bannerSub.text =
                "Preparing (Arriving in ${order.waitTime})"

            binding.activeOrderAction.text =
                "Track"
        }
    }
    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }
}