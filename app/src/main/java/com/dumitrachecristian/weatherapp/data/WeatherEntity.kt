package com.dumitrachecristian.weatherapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dumitrachecristian.weatherapp.UiState

@Entity(tableName = "WEATHER_ENTITY")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "TEMPERATURE")
    val temperature: String = "",

    @ColumnInfo(name = "FEELS_LIKE")
    val feelsLike: String = "",

    @ColumnInfo(name = "WEATHER_MAIN_DESCRIPTION")
    val weatherMainDescription: String = "",

    @ColumnInfo(name = "BACKGROUND_IMAGE")
    val backgroundImage: Int = 0,

//    @ColumnInfo(name = "MAIN_COLOR")
    //   val mainColor: Color = 0,

    @ColumnInfo(name = "MAX_TEMPERATURE")
    val maxTemperature: String = "",

    @ColumnInfo(name = "MIN_TEMPERATURE")
    val minTemperature: String = "",

    @ColumnInfo(name = "SUNRISE")
    val sunrise: String = "",

    @ColumnInfo(name = "SUNSET")
    val sunset: String = "",

    @ColumnInfo(name = "VISIBILITY")
    val visibility: String = "",

    @ColumnInfo(name = "ADDRESS")
    val address: String = "",

    @ColumnInfo(name = "LAST_UPDATE")
    val lastUpdateTime: String = "",

    // @ColumnInfo(name = "FORECAST")
    // val forecast: Forecast = "",
)

fun WeatherEntity.toUiState(): UiState {
    return UiState(
        temperature = temperature,
        feelsLike = feelsLike,
        weatherMainDescription = weatherMainDescription,
        backgroundImage = backgroundImage,
        mainColor = null,
        minTemperature = minTemperature,
        maxTemperature = maxTemperature,
        sunrise = sunrise,
        sunset = sunset,
        visibility = visibility,
        address = address,
        forecast = null
    )
}