package com.dumitrachecristian.weatherapp.ui.settingsscreen


import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.dumitrachecristian.weatherapp.ui.mainscreen.viewmodel.MainViewModel
import com.dumitrachecristian.weatherapp.R
import com.dumitrachecristian.weatherapp.model.uimodel.MenuItem
import com.dumitrachecristian.weatherapp.ui.theme.Cloudy
import com.dumitrachecristian.weatherapp.ui.theme.TestAppComposeTheme

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: MainViewModel
) {
    TestAppComposeTheme() {
        Box(
            modifier = Modifier
                .background(Cloudy)
                .fillMaxSize()
        ) {
            val context = LocalContext.current
            SettingsMenuItem(stringResource(R.string.units), getUnitsMenuItem(context), viewModel.getUnit()) {
                viewModel.updateUnit(it)
            }
            IconButton(
                modifier = Modifier
                    .padding(top = 20.dp, start = 10.dp),
                onClick = {
                    navController.popBackStack()
                }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = Color.Black
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsMenuItem(label: String, options: List<MenuItem>, selectedItem: String, onClick: (MenuItem) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf( options.find { it.value == selectedItem } ?: options[0]) }

    ExposedDropdownMenuBox(
        modifier = Modifier.fillMaxWidth().padding(20.dp)
            .padding(top = 50.dp),
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            modifier = Modifier.menuAnchor().fillMaxWidth(),
            readOnly = true,
            value = selectedOptionText.label,
            onValueChange = { },
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        DropdownMenu(
            modifier = Modifier.exposedDropdownSize(),
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(text = selectionOption.label)
                    },
                    onClick = {
                        selectedOptionText = selectionOption
                        expanded = false
                        onClick.invoke(selectionOption)
                    }
                )
            }
        }
    }
}

private fun getUnitsMenuItem(context: Context): List<MenuItem> {
    return listOf(
        MenuItem(value = "metric", label = context.getString(R.string.metric)),
        MenuItem(value = "imperial", label = context.getString(R.string.imperial))
    )
}