package com.dumitrachecristian.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherModelResponse(
    @SerializedName("weather") val weather: List<Weather>? = null,
    @SerializedName("main") val weatherMain: WeatherMain? = null,
    @SerializedName("visibility") val visibility: Int? = null,
    @SerializedName("wind") val wind: Wind? = null,
    @SerializedName("dt") val time: Long? = null,
    @SerializedName("sys") val sys: Sys? = null,
)