package com.ryz.mystore.utils

sealed class GenericState<T> {
    class Success<T>(val data: T) : GenericState<T>()
    class Loading<T>(val isLoading: Boolean) : GenericState<T>()
    class Error<T>(var message: String? = null) : GenericState<T>()
}