package com.example.coinhakodemo.model

import retrofit2.Response


sealed class BaseDataState {
    object Idle : BaseDataState()
    class Success<T>(val data: T) : BaseDataState()
    object Loading : BaseDataState()
    class Error(val message: String) : BaseDataState()
}
