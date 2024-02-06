package com.dumitrachecristian.weatherapp.ui.components.weather

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.dumitrachecristian.weatherapp.ui.theme.Typography

@Composable
fun CurrentWeatherInformationItem(
    modifier: Modifier = Modifier,
    value: String?,
    @DrawableRes drawableRes: Int
) {
    Column(modifier = modifier) {
        Icon(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            painter = painterResource(drawableRes),
            contentDescription = null,
            tint = Color.White
        )

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text = value ?: "",
            color = Color.White,
            style = Typography.titleLarge,
            )
    }
}