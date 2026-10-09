package com.ruchir55.brewkery.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import coil.load
import com.ruchir55.brewkery.R
import com.ruchir55.brewkery.databinding.ActivityItemDetailBinding
import com.ruchir55.brewkery.model.MenuItem
import com.ruchir55.brewkery.network.RetrofitClient
import com.ruchir55.brewkery.repository.BrewkeryRepository
import com.ruchir55.brewkery.model.CartItem
import com.ruchir55.brewkery.state.CartManager

import kotlinx.coroutines.launch
import java.util.Locale

class ItemDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityItemDetailBinding

    private val repository =
        BrewkeryRepository(RetrofitClient.api)

    private var currentItem: MenuItem? = null

    private var selectedSizeIndex = 0

    private var selectedMilkIndex = 0

    private var selectedSugarIndex = 0

    private var quantity = 1

    private var favorite = false

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityItemDetailBinding.inflate(
                layoutInflater
            )

        setContentView(binding.root)

        setupListeners()

        val itemId =
            intent.getIntExtra(
                "itemId",
                -1
            )

        if (itemId == -1) {

            finish()

            return
        }

        loadItem(itemId)
    }

    private fun setupListeners() {

        binding.backButton.setOnClickListener {
            finish()
        }

        binding.minusButton.setOnClickListener {

            if (quantity > 1) {

                quantity--

                binding.quantityText.text =
                    quantity.toString()

                updateTotal()
            }
        }

        binding.plusButton.setOnClickListener {

            quantity++

            binding.quantityText.text =
                quantity.toString()

            updateTotal()
        }

        binding.favoriteButton.setOnClickListener {

            favorite = !favorite

            binding.favoriteButton.text =
                if (favorite)
                    "♥"
                else
                    "♡"

            binding.favoriteButton.setTextColor(
                if (favorite)
                    Color.rgb(217, 83, 47)
                else
                    Color.rgb(120, 100, 87)
            )
        }

        binding.addToCartButton.setOnClickListener {

            addToCart()
        }
    }

    private fun loadItem(
        itemId: Int
    ) {

        showLoading(true)

        lifecycleScope.launch {

            try {

                val item =
                    repository.getItem(itemId)

                currentItem =
                    item

                displayItem(item)

                showLoading(false)

            } catch (e: Exception) {

                showLoading(false)

                binding.errorText.visibility =
                    View.VISIBLE

                binding.errorText.text =
                    "Unable to load this item. Please check your connection."
            }
        }
    }

    private fun displayItem(
        item: MenuItem
    ) {

        binding.heroImage.load(
            item.image_url
        ) {
            crossfade(true)
            placeholder(
                R.drawable.ic_image_placeholder
            )
            error(
                R.drawable.ic_image_placeholder
            )
        }

        binding.badgeText.text =
            item.badge

        binding.itemName.text =
            item.name

        binding.basePrice.text =
            String.format(
                Locale.US,
                "$%.2f",
                item.base_price
            )

        binding.descriptionText.text =
            item.description

        binding.ingredientsText.text =
            item.ingredients.joinToString(
                separator = "   •   "
            )

        quantity = 1

        binding.quantityText.text =
            "1"

        setupSizeOptions(item)

        setupMilkOptions(item)

        setupSugarOptions(item)

        updateTotal()
    }

    private fun setupSizeOptions(item: MenuItem) {
        binding.sizeGroup.removeAllViews()

        item.customizations.sizes.forEachIndexed { index, option ->

            val radioButton = RadioButton(this).apply {
                id = View.generateViewId()
                text = "${option.label}                       ${
                    String.format(Locale.US, "+$%.2f", option.extra_price)
                }"
                textSize = 14f
                setTextColor(resources.getColor(R.color.muted_text, theme))
                buttonTintList = android.content.res.ColorStateList.valueOf(
                    resources.getColor(R.color.terracotta, theme)
                )

                layoutParams = android.widget.RadioGroup.LayoutParams(
                    android.widget.RadioGroup.LayoutParams.MATCH_PARENT,
                    android.widget.RadioGroup.LayoutParams.WRAP_CONTENT
                )

                isChecked = index == selectedSizeIndex
            }

            binding.sizeGroup.addView(radioButton)

            radioButton.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    selectedSizeIndex = index
                    updateTotal()
                }
            }
        }
    }

    private fun setupMilkOptions(item: MenuItem) {
        binding.milkGroup.removeAllViews()

        item.customizations.milk_options.forEachIndexed { index, option ->

            val radioButton = RadioButton(this).apply {
                id = View.generateViewId()
                text = "${option.name}                       ${
                    String.format(Locale.US, "+$%.2f", option.extra_price)
                }"
                textSize = 14f
                setTextColor(resources.getColor(R.color.muted_text, theme))
                buttonTintList = android.content.res.ColorStateList.valueOf(
                    resources.getColor(R.color.terracotta, theme)
                )

                layoutParams = android.widget.RadioGroup.LayoutParams(
                    android.widget.RadioGroup.LayoutParams.MATCH_PARENT,
                    android.widget.RadioGroup.LayoutParams.WRAP_CONTENT
                )

                isChecked = index == selectedMilkIndex
            }

            binding.milkGroup.addView(radioButton)

            radioButton.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    selectedMilkIndex = index
                    updateTotal()
                }
            }
        }
    }

    private fun setupSugarOptions(item: MenuItem) {

        binding.sugarGroup.removeAllViews()

        item.customizations.sugar_levels.forEachIndexed { index, option ->

            val radioButton = LayoutInflater.from(this)
                .inflate(
                    R.layout.sugar_option_row,
                    binding.sugarGroup,
                    false
                ) as RadioButton

            radioButton.id = View.generateViewId()
            radioButton.text = option
            radioButton.isChecked = index == selectedSugarIndex

            radioButton.setOnCheckedChangeListener { _, checked ->
                if (checked) {
                    selectedSugarIndex = index

                    for (i in 0 until binding.sugarGroup.childCount) {
                        val otherButton =
                            binding.sugarGroup.getChildAt(i) as RadioButton

                        if (otherButton.id != radioButton.id) {
                            otherButton.isChecked = false
                        }
                    }
                }
            }

            binding.sugarGroup.addView(radioButton)
        }
    }

    private fun createOptionRadioButton(
        title: String,
        price: String
    ): RadioButton {

        val radioButton =
            LayoutInflater
                .from(this)
                .inflate(
                    R.layout.option_row,
                    null
                ) as RadioButton

        radioButton.text =
            if (price.isEmpty())
                title
            else
                "$title    $price"

        return radioButton
    }

    private fun updateTotal() {

        val item =
            currentItem
                ?: return

        val sizeExtra =
            item.customizations
                .sizes
                .getOrNull(
                    selectedSizeIndex
                )
                ?.extra_price
                ?: 0.0

        val milkExtra =
            item.customizations
                .milk_options
                .getOrNull(
                    selectedMilkIndex
                )
                ?.extra_price
                ?: 0.0

        val total =
            (
                    item.base_price +
                            sizeExtra +
                            milkExtra
                    ) * quantity

        binding.addToCartButton.text =
            String.format(
                Locale.US,
                "Add to Cart  •  $%.2f",
                total
            )
    }

    private fun addToCart() {

        val item =
            currentItem
                ?: return

        val size =
            item.customizations
                .sizes
                .getOrNull(
                    selectedSizeIndex
                )
                ?: item.customizations.sizes.first()

        val milk =
            item.customizations
                .milk_options
                .getOrNull(
                    selectedMilkIndex
                )
                ?: item.customizations
                    .milk_options
                    .first()

        val sugar =
            item.customizations
                .sugar_levels
                .getOrNull(
                    selectedSugarIndex
                )
                ?: item.customizations
                    .sugar_levels
                    .first()

        val unitPrice =
            item.base_price +
                    size.extra_price +
                    milk.extra_price

        val cartItem =
            CartItem(
                id = item.id,
                name = item.name,
                size = size.label,
                milk = milk.name,
                sugar = sugar,
                unitPrice = unitPrice,
                quantity = quantity
            )

        CartManager.add(
            cartItem
        )

        startActivity(
            Intent(
                this,
                CartActivity::class.java
            )
        )

        finish()
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
//    private fun setupExclusiveSelection(
//        group: android.view.ViewGroup,
//        radioButton: RadioButton,
//        index: Int,
//        onSelected: (Int) -> Unit
//    ) {
//        radioButton.setOnClickListener {
//            for (i in 0 until group.childCount) {
//                val row = group.getChildAt(i)
//
//                val otherButton = row.findViewById<RadioButton>(
//                    R.id.optionRadioButton
//                )
//
//                if (otherButton != null && otherButton != radioButton) {
//                    otherButton.isChecked = false
//                }
//            }
//
//            radioButton.isChecked = true
//            onSelected(index)
//        }


    private fun selectOnlyOne(
        group: android.view.ViewGroup,
        selectedButton: RadioButton,
        onSelected: () -> Unit
    ) {
        for (i in 0 until group.childCount) {
            val row = group.getChildAt(i)

            val button = row.findViewById<RadioButton>(
                R.id.optionRadioButton
            )

            if (button != null) {
                button.isChecked = button == selectedButton
            }
        }

        onSelected()
    }
}