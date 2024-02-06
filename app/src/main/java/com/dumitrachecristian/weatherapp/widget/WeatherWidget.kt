package com.dumitrachecristian.weatherapp.widget

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.actionStartActivity
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.dumitrachecristian.weatherapp.MainActivity
import com.dumitrachecristian.weatherapp.R
import com.dumitrachecristian.weatherapp.data.WeatherEntity
import com.dumitrachecristian.weatherapp.utils.Utils


class WeatherWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = WidgetUtils.getWeatherRepository(context)
        val utils = WidgetUtils.getUtils(context)
        val weatherEntity = repository.getCurrentLocationFromDb()
        provideContent {
            WeatherWidgetContent(context, weatherEntity, utils)
        }
    }

    override val stateDefinition: GlanceStateDefinition<*>?
        get() = super.stateDefinition

    @Composable
    private fun WeatherWidgetContent(context: Context, weatherEntity: WeatherEntity, utils: Utils) {
        val intent = Intent(context, MainActivity::class.java)
        Box(
            modifier = GlanceModifier.fillMaxWidth()
        ) {
            utils.getBackgroundImage(weatherEntity.conditionId)?.let { resId ->
                Image(
                    modifier = GlanceModifier.padding(top = 20.dp)
                        .clickable(
                            actionStartActivity(intent)
                        ),
                    provider = ImageProvider(resId),
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }
            Image(
                modifier = GlanceModifier.height(30.dp)
                    .fillMaxWidth(),
                provider = ImageProvider(R.drawable.gradient_background),
                contentDescription = ""
            )

            Column {
                Row() {
                    Text(
                        modifier = GlanceModifier.padding(3.dp),
                        text = utils.formatTemperature(weatherEntity.temperature.toInt()),
                        style = TextStyle(
                            color = ColorProvider(Color.White),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(GlanceModifier.width(15.dp))
                    Row(
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            modifier = GlanceModifier.padding(3.dp)
                                .defaultWeight(),
                            text = utils.getDateTimeFromTimeSeconds(weatherEntity.time),
                            style = TextStyle(color = ColorProvider(Color.White), fontSize = 8.sp)
                        )
                        Image(
                            modifier = GlanceModifier.padding(top = 3.dp)
                                .size(15.dp)
                                .clickable{
                                    WeatherWorker.startWorker(context)
                                },
                            provider = ImageProvider(R.drawable.ic_refresh),
                            colorFilter = ColorFilter.tint(ColorProvider(Color.White)),
                            contentDescription = "",
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                Text(
                    modifier = GlanceModifier.padding(5.dp),
                    text = weatherEntity.address,
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    modifier = GlanceModifier.padding(5.dp)
                        .defaultWeight(),
                    text = weatherEntity.conditionName,
                    style = TextStyle(
                        color = ColorProvider(Color.White),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}