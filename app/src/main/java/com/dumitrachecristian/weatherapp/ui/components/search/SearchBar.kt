package com.dumitrachecristian.weatherapp.ui.components.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.dumitrachecristian.weatherapp.ui.mainscreen.viewmodel.MainViewModel
import com.dumitrachecristian.weatherapp.utils.useDebounce
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarComponent(viewModel: MainViewModel, drawerState: DrawerState, scope: CoroutineScope) {
    var searchText by remember { mutableStateOf("") }
    val searchActive = remember { mutableStateOf(false) }
    val searchResultList = viewModel.searchResult

    searchText.useDebounce {
        viewModel.searchLocation(it)
    }
    SearchBar(modifier = Modifier
        .padding(top = 18.dp)
        .fillMaxWidth()
        .padding(horizontal = if (searchActive.value) 0.dp else 14.dp),
        tonalElevation = 10.dp,
        shape = RoundedCornerShape(10.dp),
        colors = SearchBarDefaults.colors(
            containerColor = Color.LightGray, dividerColor = Color.Black,
            inputFieldColors = SearchBarDefaults.inputFieldColors()
        ),
        query = searchText,
        onQueryChange = {
            searchText = it
        },
        onSearch = {
            searchActive.value = false
            scope.launch {
                drawerState.close()
            }
        },
        active = searchActive.value,
        onActiveChange = {
            searchActive.value = it
        },
        placeholder = {
            Text(text = "Search for a city")
        },
        leadingIcon = {
            if (searchActive.value) {
                Icon(imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back icon",
                    modifier = Modifier
                        .clickable {
                            searchActive.value = false
                        })
            } else {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu icon")
            }
        }) {
        LazyColumn {
            val modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp)
            items(searchResultList.size) { position ->
                SearchResultItem(
                    modifier = modifier,
                    searchResult = searchResultList[position],
                    onFavoriteClick = { searchResult ->
                        viewModel.addRemoveFromFavorites(searchResult)
                    },
                    onClick = { searchResult ->
                        searchActive.value = false
                        scope.launch {
                            drawerState.close()
                        }
                        viewModel.getDataForLocation(searchResult.latitude, searchResult.longitude, searchResult.id)
                    }
                )
            }
        }
    }
}