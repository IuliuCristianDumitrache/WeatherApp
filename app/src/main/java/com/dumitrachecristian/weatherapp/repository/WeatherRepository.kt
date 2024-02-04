package com.dumitrachecristian.weatherapp.repository

import com.dumitrachecristian.weatherapp.data.SessionManager
import com.dumitrachecristian.weatherapp.model.ForecastModelResponse
import com.dumitrachecristian.weatherapp.model.WeatherModelResponse
import com.dumitrachecristian.weatherapp.network.ApiService
import com.dumitrachecristian.weatherapp.network.Result
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val apiService: ApiService,
    private val sessionManager: SessionManager
): BaseRepository {

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