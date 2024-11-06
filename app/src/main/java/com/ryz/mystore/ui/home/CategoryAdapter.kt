package com.ryz.mystore.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ryz.mystore.R
import com.ryz.mystore.databinding.ItemCategoryBinding
import com.ryz.mystore.utils.BaseAdapter
import com.ryz.mystore.utils.toCapitalizeWords

class CategoryAdapter(private val onClick: (String) -> Unit) :
    BaseAdapter<String, ItemCategoryBinding>(DIFF_CALLBACK) {
    private var selectedPosition = RecyclerView.NO_POSITION

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ItemCategoryBinding
        get() = ItemCategoryBinding::inflate

    override fun bind(binding: ItemCategoryBinding, item: String, position: Int) {
        with(binding) {
            tvCategory.text = item.toCapitalizeWords()
            val bgColor =
                if (position == selectedPosition) R.color.md_theme_primaryContainer else R.color.white
            root.setCardBackgroundColor(ContextCompat.getColor(root.context, bgColor))

            root.setOnClickListener {
                val previousPosition = selectedPosition
                selectedPosition = if (position == selectedPosition) {
                    RecyclerView.NO_POSITION
                } else {
                    position
                }
                notifyItemChanged(previousPosition)
                notifyItemChanged(selectedPosition)

                onClick.invoke(item)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<String>() {
            override fun areItemsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
                oldItem == newItem
        }
    }
}