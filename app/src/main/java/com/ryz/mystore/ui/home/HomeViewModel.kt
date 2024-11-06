package com.ryz.mystore.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryz.mystore.data.repository.ProductRepository
import com.ryz.mystore.utils.GenericState
import com.ryz.mystore.utils.updateWithResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<GenericState<HomeState>>(GenericState.Loading(false))
    val uiState = _uiState.asStateFlow()

    fun getAllProduct() = viewModelScope.launch {
        repository.getAllProduct().collectLatest { resource ->
            _uiState.updateWithResource(resource) { data ->
                HomeState(allProduct = data)
            }
        }
    }

    fun getProductByCategory(category: String) = viewModelScope.launch {
        repository.getProductByCategory(category).collectLatest { resource ->
            _uiState.updateWithResource(resource) { data ->
                HomeState(allProduct = data)
            }
        }
    }

    fun getCategory() = viewModelScope.launch {
        repository.getCategory().collectLatest { resource ->
            _uiState.updateWithResource(resource) { data ->
                HomeState(category = data)
            }
        }
    }

    fun getProductDetail(id: Int) = viewModelScope.launch {
        repository.getProductDetail(id).collectLatest { resource ->
            _uiState.updateWithResource(resource) { data ->
                HomeState(productDetail = data)
            }
        }
    }
}