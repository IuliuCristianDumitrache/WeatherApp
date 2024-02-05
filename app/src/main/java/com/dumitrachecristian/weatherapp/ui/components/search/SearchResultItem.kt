package com.dumitrachecristian.weatherapp.ui.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dumitrachecristian.weatherapp.R
import com.dumitrachecristian.weatherapp.model.uimodel.SearchResult
import com.dumitrachecristian.weatherapp.ui.theme.Rainy

@Composable
fun SearchResultItem(
    modifier: Modifier,
    searchResult: SearchResult,
    onFavoriteClick: (SearchResult) -> Unit,
    onClick: (SearchResult) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Rainy),
        shape = RoundedCornerShape(10.dp),
        modifier = modifier.clickable {
            onClick.invoke(searchResult)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = searchResult.address,
                modifier = Modifier.align(Alignment.Start),
                color = Color.White
            )

            IconButton(
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.End)
                    .padding(start = 10.dp),

                onClick = {
                    onFavoriteClick.invoke(searchResult)
                }) {

                Icon(
                    painterResource(if (searchResult.isFavorite) R.drawable.ic_favorite_fill else R.drawable.ic_favorite_outlined),
                    contentDescription = stringResource(R.string.favorite_content_description),
                    tint = Color.Red
                )
            }
        }
    }
}