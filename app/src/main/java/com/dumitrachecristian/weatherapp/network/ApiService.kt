package com.dumitrachecristian.weatherapp.network

import com.dumitrachecristian.weatherapp.model.ForecastModelResponse
import com.dumitrachecristian.weatherapp.model.WeatherModelResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("units") units: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<WeatherModelResponse>

    @GET("forecast")
    suspend fun getForecast(
        @Query("units") units: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): Response<ForecastModelResponse>
}