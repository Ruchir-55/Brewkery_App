package com.ruchir55.brewkery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ruchir55.brewkery.databinding.ItemCartBinding
import com.ruchir55.brewkery.model.CartItem
import java.util.Locale

class CartAdapter(
    private var items: List<CartItem>,
    private val onQuantityChanged: (Int, Int) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(
        private val binding: ItemCartBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItem) {

            binding.itemName.text =
                item.name

            binding.customizationText.text =
                "${item.size} • ${item.milk}\n${item.sugar}"

            binding.quantityText.text =
                item.quantity.toString()

            binding.itemTotal.text =
                String.format(
                    Locale.US,
                    "$%.2f",
                    item.totalPrice
                )

            binding.minusButton.setOnClickListener {

                onQuantityChanged(
                    bindingAdapterPosition,
                    -1
                )
            }

            binding.plusButton.setOnClickListener {

                onQuantityChanged(
                    bindingAdapterPosition,
                    1
                )
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartViewHolder {

        val binding =
            ItemCartBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: CartViewHolder,
        position: Int
    ) {

        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(
        newItems: List<CartItem>
    ) {

        items = newItems

        notifyDataSetChanged()
    }
}