package com.dumitrachecristian.weatherapp.model

import com.google.gson.annotations.SerializedName

//Rain/Snow volume for the last 1 hour or last 3h, mm. Please note that only mm as units of measurement are available for this parameter
data class RainOrSnow(
    @SerializedName("1h") val oneHour : Double? = null,
    @SerializedName("3h") val threeHours : Double? = null,
)
