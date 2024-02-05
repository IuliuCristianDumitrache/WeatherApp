package com.dumitrachecristian.weatherapp.data.datasource

import com.dumitrachecristian.weatherapp.data.WeatherEntity
import com.dumitrachecristian.weatherapp.data.WeatherEntityDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherLocalDataSource @Inject constructor(
    private val weatherEntityDao: WeatherEntityDao
){

    suspend fun getWeatherByAddressId(addressId: String): WeatherEntity {
        return weatherEntityDao.getWeatherByAddressId(addressId = addressId)
    }

    suspend fun insert(weatherEntity: WeatherEntity) {
        weatherEntityDao.insert(weatherEntity)
    }

    suspend fun remove(addressId: String) {
        weatherEntityDao.remove(addressId)
    }

    fun getFavoriteLocations(): Flow<List<WeatherEntity>> {
        return weatherEntityDao.getFavoriteLocations()
    }

    suspend fun getFavoriteLocationsSuspend(): List<WeatherEntity> {
        return weatherEntityDao.getFavoriteLocationsSuspend()
    }
}