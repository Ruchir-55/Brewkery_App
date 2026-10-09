package com.ruchir55.brewkery.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ruchir55.brewkery.R
import com.ruchir55.brewkery.databinding.ItemMenuBinding
import com.ruchir55.brewkery.model.MenuItem
import java.util.Locale

class MenuAdapter(
    private var items: List<MenuItem>,
    private val onItemClick: (MenuItem) -> Unit
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(
        private val binding: ItemMenuBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MenuItem) {

            binding.itemImage.load(item.image_url) {
                crossfade(true)
                placeholder(R.drawable.ic_image_placeholder)
                error(R.drawable.ic_image_placeholder)
            }

            binding.badgeText.text =
                item.badge

            binding.itemName.text =
                item.name

            binding.ratingText.text =
                "⭐ ${item.rating} (${item.review_count})"

            binding.priceText.text =
                String.format(
                    Locale.US,
                    "$%.2f",
                    item.base_price
                )

            binding.root.setOnClickListener {
                onItemClick(item)
            }

            binding.customizeButton.setOnClickListener {
                onItemClick(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuViewHolder {

        val binding =
            ItemMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MenuViewHolder,
        position: Int
    ) {

        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateItems(
        newItems: List<MenuItem>
    ) {

        items = newItems

        notifyDataSetChanged()
    }
}