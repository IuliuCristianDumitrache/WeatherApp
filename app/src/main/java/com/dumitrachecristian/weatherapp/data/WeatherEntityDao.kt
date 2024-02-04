package com.dumitrachecristian.weatherapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.dumitrachecristian.weatherapp.data.WeatherEntity

@Dao
interface WeatherEntityDao {
    @Query("SELECT * FROM WEATHER_ENTITY WHERE ADDRESS = :address")
    fun getWeather(address: String): List<WeatherEntity>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(weatherEntity: WeatherEntity)
}