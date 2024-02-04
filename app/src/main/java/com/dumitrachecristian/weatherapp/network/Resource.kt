package com.dumitrachecristian.weatherapp.network

import java.io.Serializable

sealed class Result<T>(
    val httpCode: Int? = null,
    val data: T? = null,
    val message: String? = null,
) : Serializable {
    class Success<T>(data: T) : Result<T>(data = data)
    class Loading<T> : Result<T>()
    class Error<T>(httpCode: Int? = null, message: String?) :
        Result<T>(httpCode = httpCode, message = message)
}