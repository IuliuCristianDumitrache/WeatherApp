package com.dumitrachecristian.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Wind(
    @SerializedName("speed") val speed: Double? = null,
    @SerializedName("deg") val directionDegrees: Int? = null,
    @SerializedName("gust") val gust: Double? = null,
)