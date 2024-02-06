package com.dumitrachecristian.weatherapp

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.dumitrachecristian.weatherapp.navigation.NavGraph
import com.dumitrachecristian.weatherapp.ui.mainscreen.viewmodel.MainViewModel
import com.dumitrachecristian.weatherapp.ui.theme.TestAppComposeTheme
import com.dumitrachecristian.weatherapp.utils.Utils
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var backgroundPermissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        val viewModel: MainViewModel by viewModels()

        backgroundPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                // Permission granted
            } else {
                // Handle permission denial
            }
        }

        permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            var permissionGranted = true
            permissions.entries.forEach {
                if (!it.value) {
                    permissionGranted = false
                }
            }
            viewModel.setPermissionGranted(permissionGranted)

            askBackgroundPermission()
        }
        permissionLauncher.launch(Utils.getLocationPermissions())

        setContent {
            val navController = rememberNavController()
            TestAppComposeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavGraph(viewModel, navController = navController)
                }
            }
        }
    }

    private fun askBackgroundPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
            } else {
                askPermissionForBackgroundUsage()
            }
        }
    }

    private fun askPermissionForBackgroundUsage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
            ) {
                return
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this@MainActivity,
                        android.Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    )
                ) {
                    AlertDialog.Builder(this)
                        .setTitle(getString(R.string.permission_needed_for_widget))
                        .setMessage(getString(R.string.background_location_permission_needed_tap_allow_all_time_in_the_next_screen))
                        .setPositiveButton(
                            getString(R.string.ok)
                        ) { dialog, which ->
                            backgroundPermissionLauncher.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                        }
                        .setNegativeButton(
                            getString(R.string.cancel)
                        ) { dialog, which ->
                        }
                        .create().show()
                } else {
                    backgroundPermissionLauncher.launch(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                }
            }
        }
    }
}