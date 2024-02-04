package com.dumitrachecristian.weatherapp.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.compose.ui.graphics.Color
import com.dumitrachecristian.weatherapp.R
import com.dumitrachecristian.weatherapp.model.WeatherModelResponse
import com.dumitrachecristian.weatherapp.ui.theme.Cloudy
import com.dumitrachecristian.weatherapp.ui.theme.Rainy
import com.dumitrachecristian.weatherapp.ui.theme.Sunny
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.coroutines.resume

class Utils @Inject constructor(@ApplicationContext private val context: Context) {

    private val DRIZZLE_CODE = 300..321
    private val THUNDERSTORM_CODES = 200..232
    private val RAIN_CODE = 500..531
    private val SNOW_CODE = 600..622
    private val ATMOSPHERE_CODE = 701..781
    private val CLEAR_CODE = 800..800
    private val CLOUDS_CODE = 801..804

    fun getIconUrl(iconId: String?): String? {
        if (iconId == null) {
            return null
        }
        return "https://openweathermap.org/img/wn/${iconId}.png"
    }

    fun getBackgroundImage(code: Int?): Int? {
        return when (code) {
            in CLOUDS_CODE, in THUNDERSTORM_CODES, in ATMOSPHERE_CODE, in SNOW_CODE -> {
                 R.drawable.forest_cloudy
            }
            in CLEAR_CODE -> {
                R.drawable.forest_sunny
            }
            in RAIN_CODE, in DRIZZLE_CODE -> {
                R.drawable.forest_rainy
            }

            else -> null
        }
    }

    fun formatTemperature(temperature: Int?): String {
        return "${(temperature?.toString() ?: "")}°"
    }

    fun formatFeelsLikeTemperature(temperature: Int?): String {
        return context.getString(R.string.feels_like, "${(temperature?.toString() ?: "")}°")
    }

    fun getMainColor(code: Int?): Color? {
        return when (code) {
            in CLOUDS_CODE, in THUNDERSTORM_CODES, in ATMOSPHERE_CODE -> {
                Cloudy
            }
            in CLEAR_CODE -> {
                Sunny
            }
            in RAIN_CODE, in DRIZZLE_CODE -> {
                Rainy
            }

            else -> null
        }
    }

    fun getDayFromTimeMillis(time: Long?): String {
        if (time == null) {
            return ""
        }
        val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
        val date = Date()
        date.time = time * 1000
        return sdf.format(date)
    }

    fun getTimeFromTimeMillis(time: Long?): String {
        if (time == null) {
            return ""
        }
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val date = Date()
        date.time = time * 1000
        return sdf.format(date)
    }

    fun formatVisibilityInKm(visibilityInMeters: Int?): String {
        if (visibilityInMeters == null) {
            return ""
        }
        if (visibilityInMeters == 10000) {
            return context.getString(R.string.unlimited)
        }
        return context.getString(R.string.km, (visibilityInMeters / 1000.toDouble()).toString())
    }

    suspend fun getAddress(latitude: Double?, longitude: Double?) : Address? {
        if (latitude == null) {
            return null
        }
        if (longitude == null) {
            return null
        }
        return suspendCancellableCoroutine { cancellableContinuation ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                Geocoder(context).getFromLocation(
                    latitude, longitude, 1
                ) { list -> // Geocoder.GeocodeListener
                    list.firstOrNull()?.let { address ->
                        cancellableContinuation.resume(
                            address
                        )
                    }
                }
            } else {
                val address = Geocoder(context).getFromLocation(latitude, longitude, 1)
                cancellableContinuation.resume(address?.firstOrNull())
            }
        }
    }

//    fun formatForecastMedian(list: List<WeatherModelResponse>?): Any {
//        if (list.isNullOrEmpty()) {
//            return null
//        }
//        val hashMap = HashMap<String, T>()
//        list.forEach { weather ->
//            hashMap.getOrPut(getDayFromTimeMillis(weather.time))
//        }
//    }

    data class T(
        val temperature: Double,
        val iconId: Int
    )
}