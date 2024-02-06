package com.dumitrachecristian.weatherapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dumitrachecristian.weatherapp.utils.Utils
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherEntityDao {
    @Query("SELECT * FROM WEATHER_ENTITY WHERE ADDRESS_ID = :addressId")
    suspend fun getWeatherByAddressId(addressId: String): WeatherEntity

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(weatherEntity: WeatherEntity)

    @Query("DELETE FROM WEATHER_ENTITY WHERE ADDRESS_ID = :addressId")
    suspend fun remove(addressId: String)

    @Query("SELECT * FROM WEATHER_ENTITY")
    fun getFavoriteLocations(): Flow<List<WeatherEntity>>

    @Query("SELECT * FROM WEATHER_ENTITY WHERE ADDRESS_ID = :addressId")
    suspend fun getCurrentLocation(addressId: String = Utils.CURRENT_LOCATION): WeatherEntity

    @Query("SELECT * FROM WEATHER_ENTITY")
    suspend fun getFavoriteLocationsSuspend(): List<WeatherEntity>
}