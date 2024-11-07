package com.ryz.mystore.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.ryz.mystore.R
import com.ryz.mystore.data.model.response.ProductResponse
import com.ryz.mystore.databinding.ItemProductBinding
import com.ryz.mystore.utils.BaseAdapter
import com.ryz.mystore.utils.loadImageUrl

class ProductAdapter(private val onClick: (Int?) -> Unit) :
    BaseAdapter<ProductResponse, ItemProductBinding>(DIFF_CALLBACK) {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> ItemProductBinding
        get() = ItemProductBinding::inflate

    override fun bind(binding: ItemProductBinding, item: ProductResponse, position: Int) {
        with(binding) {
            ivProduct.loadImageUrl(item.image)
            tvProductName.text = item.title
            tvRating.text = item.rating?.rate.toString()
            tvCountRating.text = root.context.getString(R.string.count_rating, item.rating?.count.toString())
            tvPrice.text = root.context.getString(R.string.format_price, item.price.toString())

            root.setOnClickListener {
                onClick.invoke(item.id)
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProductResponse>() {
            override fun areItemsTheSame(
                oldItem: ProductResponse,
                newItem: ProductResponse
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ProductResponse,
                newItem: ProductResponse
            ): Boolean = oldItem == newItem
        }
    }
}