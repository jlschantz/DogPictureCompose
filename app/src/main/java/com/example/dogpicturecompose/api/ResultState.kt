package com.example.dogpicturecompose.api

sealed class ResultState<T> (
    val data: T? = null,
    val error: String? = null
) {
    class Success<T>(data: T): ResultState<T>(data)
    class Loading<T>(data: T? = null): ResultState<T>(data)
    class Error<T>(data: T? = null, error: String?): ResultState<T>(data, error)
}