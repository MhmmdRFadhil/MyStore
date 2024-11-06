package com.ryz.mystore.data.repository

import com.ryz.mystore.data.model.response.ProductResponse
import com.ryz.mystore.data.remote.Resource
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProduct(): Flow<Resource<List<ProductResponse>>>
    suspend fun getProductByCategory(category: String): Flow<Resource<List<ProductResponse>>>
    suspend fun getCategory(): Flow<Resource<List<String>>>
    suspend fun getProductDetail(id: Int): Flow<Resource<ProductResponse>>
}