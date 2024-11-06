package com.ryz.mystore.data.remote

import com.ryz.mystore.data.model.response.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("/products")
    suspend fun getAllProduct(): Response<List<ProductResponse>>

    @GET("/products/category/{category}")
    suspend fun getProductByCategory(
        @Path("category") category: String
    ): Response<List<ProductResponse>>

    @GET("/products/categories")
    suspend fun getCategory(): Response<List<String>>

    @GET("/products/{product_id}")
    suspend fun getProductDetail(
        @Path("product_id") productId: Int
    ): Response<ProductResponse>
}