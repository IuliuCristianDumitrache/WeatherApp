package com.dumitrachecristian.weatherapp.repository

import com.dumitrachecristian.weatherapp.network.Result
import retrofit2.Response

interface BaseRepository {

    suspend fun <T : Any> safeApiResult(call: suspend () -> Response<T>): Result<T> {
        return try {
            val response = call.invoke()
            val body = response.body()
            if (response.isSuccessful && body != null) {
                Result.Success(body)
            } else {
                Result.Error(response.code(), response.message())
            }
        } catch (exception: Exception) {
            Result.Error(-1, exception.message)
        }
    }
}