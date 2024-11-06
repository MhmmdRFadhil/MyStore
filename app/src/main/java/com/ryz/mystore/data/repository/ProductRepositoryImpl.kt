package com.ryz.mystore.data.repository

import com.ryz.mystore.data.model.response.ProductResponse
import com.ryz.mystore.data.remote.ApiService
import com.ryz.mystore.data.remote.Resource
import com.ryz.mystore.utils.executeFlow
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(private val apiService: ApiService) :
    ProductRepository {
    override suspend fun getAllProduct(): Flow<Resource<List<ProductResponse>>> =
        executeFlow(callApi = {
            apiService.getAllProduct()
        }) { response, flow ->
            flow.emit(Resource.Success(response))
        }

    override suspend fun getProductByCategory(category: String): Flow<Resource<List<ProductResponse>>> =
        executeFlow(callApi = {
            apiService.getProductByCategory(category)
        }) { response, flow ->
            flow.emit(Resource.Success(response))
        }

    override suspend fun getCategory(): Flow<Resource<List<String>>> = executeFlow(callApi = {
        apiService.getCategory()
    }) { response, flow ->
        flow.emit(Resource.Success(response))
    }

    override suspend fun getProductDetail(id: Int): Flow<Resource<ProductResponse>> = executeFlow(callApi = {
        apiService.getProductDetail(id)
    }) { response, flow ->
        flow.emit(Resource.Success(response))
    }
}