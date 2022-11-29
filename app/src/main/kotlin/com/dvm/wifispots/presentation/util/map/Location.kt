package com.dvm.wifispots.presentation.util.map

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng

@SuppressLint("MissingPermission")
fun GoogleMap.moveToCurrentLocation(context: Context) {
    LocationServices
        .getFusedLocationProviderClient(context)
        .lastLocation
        .addOnSuccessListener { location ->
            val latLng = LatLng(location.latitude, location.longitude)
            moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, INITIAL_ZOOM))
        }
}

private const val INITIAL_ZOOM = 17f