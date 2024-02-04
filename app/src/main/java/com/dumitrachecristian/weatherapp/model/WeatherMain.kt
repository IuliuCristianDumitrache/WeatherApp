package com.dumitrachecristian.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherMain(
    @SerializedName("temp") val temperature: Double? = null,
    @SerializedName("feels_like") val feelsLike: Double? = null,
    @SerializedName("temp_min") val minTemperature: Double? = null,
    @SerializedName("temp_max") val maxTemperature: Double? = null,
    @SerializedName("pressure") val pressure: Int? = null,
    @SerializedName("humidity") val humidity: Int? = null,
    @SerializedName("sea_level") val seaLevelPressure: Int? = null,
    @SerializedName("grnd_level") val groundLevelPressure: Int? = null,
)