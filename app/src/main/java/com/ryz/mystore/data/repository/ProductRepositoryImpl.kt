package com.ryz.mystore.data.repository

import com.ryz.mystore.data.remote.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(private val apiService: ApiService): ProductRepository {
}