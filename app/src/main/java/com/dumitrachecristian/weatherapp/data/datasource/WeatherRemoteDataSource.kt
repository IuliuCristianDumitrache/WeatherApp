package com.dumitrachecristian.weatherapp.data.datasource

import com.dumitrachecristian.weatherapp.data.SessionManager
import com.dumitrachecristian.weatherapp.model.ForecastModelResponse
import com.dumitrachecristian.weatherapp.model.WeatherModelResponse
import com.dumitrachecristian.weatherapp.network.ApiService
import com.dumitrachecristian.weatherapp.network.Result
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
): BaseRemoteDataSource {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Result<WeatherModelResponse> {
        return safeApiResult {
            apiService.getCurrentWeather(sessionManager.getUnit(), latitude, longitude)
        }
    }

    suspend fun getForecast(latitude: Double, longitude: Double): Result<ForecastModelResponse> {
        return safeApiResult {
            apiService.getForecast(sessionManager.getUnit(), latitude, longitude)
        }
    }
}