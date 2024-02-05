package com.dumitrachecristian.weatherapp.model

import com.google.gson.annotations.SerializedName

data class WeatherCondition(
    @SerializedName("id") val id: Int? = null,
    @SerializedName("main") val main: String? = null, //Group of weather parameters (Rain, Snow, Clouds etc.)
    @SerializedName("description") val description: String? = null,
    @SerializedName("icon") val icon: String? = null //Weather icon id
)