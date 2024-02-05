package com.dumitrachecristian.weatherapp.ui.components.weather

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.dumitrachecristian.weatherapp.model.uimodel.Forecast

@Composable
fun ForecastItem(item: Forecast) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 20.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = item.day,
            color = Color.White,
            fontSize = 20.sp
        )
        AsyncImage(
            modifier = Modifier.size(30.dp),
            model = item.icon,
            contentDescription = null,
        )
        Text(
            modifier = Modifier.weight(1f),
            text = item.temperature ?: "",
            color = Color.White,
            fontSize = 20.sp,
            textAlign = TextAlign.End
        )
    }
}