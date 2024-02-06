package com.dumitrachecristian.weatherapp.ui.components.weather

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.dumitrachecristian.weatherapp.ui.theme.Typography

@Composable
fun CurrentWeatherItem(temperature: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = temperature,
            color = Color.White,
            style = Typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        Text(
            text = label,
            color = Color.White
        )
    }
}