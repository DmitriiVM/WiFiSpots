package com.dvm.wifispots.presentation.util.android

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts

fun ComponentActivity.requestLocationPermission(
    onPermissionGranted: () -> Unit
) {
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val isGranted = permissions.getOrDefault(
            key = Manifest.permission.ACCESS_FINE_LOCATION,
            defaultValue = false
        )
        if (isGranted) {
            onPermissionGranted()
        }
    }

    locationPermissionRequest.launch(
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    )
}