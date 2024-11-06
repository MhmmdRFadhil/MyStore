package com.ryz.mystore.ui.home

import com.ryz.mystore.data.model.response.ProductResponse

data class HomeState(
    val allProduct: List<ProductResponse>? = null,
    val category: List<String>? = null,
    val productDetail: ProductResponse? = null
)