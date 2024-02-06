package com.dumitrachecristian.weatherapp.repository

import com.dumitrachecristian.weatherapp.data.WeatherEntity
import com.dumitrachecristian.weatherapp.data.datasource.WeatherLocalDataSource
import com.dumitrachecristian.weatherapp.data.datasource.WeatherRemoteDataSource
import com.dumitrachecristian.weatherapp.model.ForecastModelResponse
import com.dumitrachecristian.weatherapp.model.WeatherModelResponse
import com.dumitrachecristian.weatherapp.network.Result
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) {

    suspend fun getCurrentWeather(latitude: Double, longitude: Double): Result<WeatherModelResponse> {

        return weatherRemoteDataSource.getCurrentWeather(latitude, longitude)
    }

    suspend fun getForecast(latitude: Double, longitude: Double): Result<ForecastModelResponse> {
        return weatherRemoteDataSource.getForecast(latitude, longitude)
    }

    suspend fun getWeatherByAddressId(addressId: String): WeatherEntity {
        return weatherLocalDataSource.getWeatherByAddressId(addressId)
    }

    suspend fun insertWeather(weatherEntity: WeatherEntity) {
        weatherLocalDataSource.insert(weatherEntity)
    }

    fun getFavoriteLocations(): Flow<List<WeatherEntity>> {
        return weatherLocalDataSource.getFavoriteLocations()
    }

    suspend fun getCurrentLocationFromDb(): WeatherEntity {
        return weatherLocalDataSource.getCurrentLocation()
    }

    suspend fun getFavoriteLocationsSuspend(): List<WeatherEntity> {
        return weatherLocalDataSource.getFavoriteLocationsSuspend()
    }

    suspend fun removeWeather(addressId: String) {
        weatherLocalDataSource.remove(addressId)
    }
}