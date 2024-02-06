package com.dumitrachecristian.weatherapp

import android.content.Context
import com.dumitrachecristian.weatherapp.model.WeatherCondition
import com.dumitrachecristian.weatherapp.model.WeatherMain
import com.dumitrachecristian.weatherapp.model.WeatherModelResponse
import com.dumitrachecristian.weatherapp.model.uimodel.Forecast
import com.dumitrachecristian.weatherapp.utils.Utils
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UtilsUnitTest {

    private val contextMock = mockk<Context>()
    private val utils: Utils = Utils(contextMock)

    @Before
    fun setup() {
        every { contextMock.getString(R.string.km, "4.0") } returns "4km"
        every { contextMock.getString(R.string.unlimited) } returns "Unlimited"
    }
    @Test
    fun test_getIconUrl() {
        val iconUrl = utils.getIconUrl("13d")

        Assert.assertEquals("https://openweathermap.org/img/wn/13d.png", iconUrl)
    }

    @Test
    fun test_getBackgroundImage() {
        Assert.assertEquals(R.drawable.forest_rainy, utils.getBackgroundImage(301))
        Assert.assertEquals(R.drawable.forest_cloudy, utils.getBackgroundImage(802))
        Assert.assertEquals(R.drawable.forest_sunny, utils.getBackgroundImage(800))
        Assert.assertEquals(R.drawable.forest_rainy, utils.getBackgroundImage(503))
    }

    @Test
    fun test_getDayFromTimeMillis() {
        Assert.assertEquals("Monday", utils.getDayFromTimeSeconds(1707146354))
    }

    @Test
    fun test_formatVisibilityInKm() {
        Assert.assertEquals("4km", utils.formatVisibilityInKm(4000))
        Assert.assertEquals("Unlimited", utils.formatVisibilityInKm(10000))
        Assert.assertEquals("Unlimited", utils.formatVisibilityInKm(12000))
    }

    @Test
    fun test_formatForecastMedian() {
        val list = listOf(
            WeatherModelResponse(
                time = 1707146354,
                weatherMain = WeatherMain(
                    23.0
                ),
                weatherConditions = arrayListOf(
                    WeatherCondition(
                        icon = "16d"
                    )
                )
            ),
            WeatherModelResponse(
                time = 1707146354,
                weatherMain = WeatherMain(
                    24.0
                ),
                weatherConditions = arrayListOf(
                    WeatherCondition(
                        icon = "13d"
                    )
                )
            ),
            WeatherModelResponse(
                time = 1707146354,
                weatherMain = WeatherMain(
                    25.0
                ),
                weatherConditions = arrayListOf(
                    WeatherCondition(
                        icon = "13d"
                    )
                )
            ),
            WeatherModelResponse(
                time = 1707573354,
                weatherMain = WeatherMain(
                    20.0
                ),
                weatherConditions = arrayListOf(
                    WeatherCondition(
                        icon = "11d"
                    )
                )
            ),
            WeatherModelResponse(
                time = 1707573354,
                weatherMain = WeatherMain(
                    10.0
                ),
                weatherConditions = arrayListOf(
                    WeatherCondition(
                        icon = "12d"
                    )
                )
            ),
            WeatherModelResponse(
                time = 1707573354,
                weatherMain = WeatherMain(
                    30.0
                ),
                weatherConditions = arrayListOf(
                    WeatherCondition(
                        icon = "13d"
                    )
                )
            )
        )

        val expectedForecast1 = Forecast(
            day = "Monday",
            temperature = "24°",
            icon = "https://openweathermap.org/img/wn/13d.png"
        )

        val expectedForecast2 = Forecast(
            day = "Saturday",
            temperature = "20°",
            icon = "https://openweathermap.org/img/wn/11d.png"
        )
        val actualForecast1 = utils.formatForecastMedian(list)?.get(0)
        val actualForecast2 = utils.formatForecastMedian(list)?.get(1)

        Assert.assertEquals(expectedForecast1.day, actualForecast1?.day)
        Assert.assertEquals(expectedForecast1.temperature, actualForecast1?.temperature)
        Assert.assertEquals(expectedForecast1.icon, actualForecast1?.icon)

        Assert.assertEquals(expectedForecast2.day, actualForecast2?.day)
        Assert.assertEquals(expectedForecast2.temperature, actualForecast2?.temperature)
        Assert.assertEquals(expectedForecast2.icon, actualForecast2?.icon)
    }
}