package com.dumitrachecristian.weatherapp.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.glance.GlanceId
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.state.GlanceStateDefinition



class WeatherWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            WeatherWidgetContent(context)
        }
    }

    override val stateDefinition: GlanceStateDefinition<*>?
        get() = super.stateDefinition

    @Composable
    private fun WeatherWidgetContent(context: Context){

    }
}