package com.dumitrachecristian.weatherapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dumitrachecristian.weatherapp.model.Sys
import com.dumitrachecristian.weatherapp.model.WeatherCondition
import com.dumitrachecristian.weatherapp.model.WeatherMain
import com.dumitrachecristian.weatherapp.model.WeatherModelResponse
import com.dumitrachecristian.weatherapp.model.uimodel.UiState

@Entity(
    tableName = "WEATHER_ENTITY",
    indices = [ Index(value = ["ADDRESS_ID"], unique = true) ],
)
data class WeatherEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ADDRESS_ID")
    var addressId: String = "",

    @ColumnInfo(name = "LAST_UPDATED")
    var lastUpdated: Long = 0,

    @ColumnInfo(name = "SUNSET")
    val sunset: Long = 0,

    @ColumnInfo(name = "SUNRISE")
    val sunrise: Long = 0,

    @ColumnInfo(name = "TIME")
    val time: Long = 0,

    @ColumnInfo(name = "VISIBILITY")
    val visibility: Int = 0,

    @ColumnInfo(name = "TEMPERATURE")
    val temperature: Double = 0.0,

    @ColumnInfo(name = "FEELS_LIKE")
    val feelsLike: Double = 0.0,

    @ColumnInfo(name = "MAX_TEMPERATURE")
    val maxTemperature: Double = 0.0,

    @ColumnInfo(name = "MIN_TEMPERATURE")
    val minTemperature: Double = 0.0,

    @ColumnInfo(name = "LATITUDE")
    var latitude: Double = 0.0,

    @ColumnInfo(name = "LONGITUDE")
    var longitude: Double = 0.0,

    @ColumnInfo(name = "CONDITION_NAME")
    val conditionName: String = "",

    @ColumnInfo(name = "CONDITION_ICON")
    val conditionIcon: String = "",

    @ColumnInfo(name = "CONDITION_ID")
    val conditionId: Int = 0,

    @ColumnInfo(name = "ADDRESS")
    var address: String = "",

    // @ColumnInfo(name = "FORECAST")
    // val forecast: Forecast = "",
)

fun WeatherEntity.toWeatherModelResponse(): WeatherModelResponse {
    return WeatherModelResponse(
        weatherConditions = arrayListOf(
            WeatherCondition(
                id = conditionId,
                icon = conditionIcon,
                main = conditionName
            )
        ),
        weatherMain = WeatherMain(
            temperature = this.temperature,
            minTemperature = this.minTemperature,
            maxTemperature = this.maxTemperature,
            feelsLike = this.feelsLike
        ),
        visibility = this.visibility,
        time = this.time,
        sys = Sys(
            sunrise = this.sunrise,
            sunset = this.sunset
        ),
        lastUpdated = this.lastUpdated,
        address = address,
        addressId = addressId,
        latitude = latitude,
        longitude = longitude
    )
}