package com.dumitrachecristian.weatherapp.model

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("country") val country: String? = null,
    @SerializedName("sunrise") val sunrise: Long? = null,
    @SerializedName("sunset") val sunset: Long? = null
)
