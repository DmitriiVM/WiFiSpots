package com.dvm.wifispots.presentation.mappers

import com.dvm.wifispots.data.model.Spot
import com.dvm.wifispots.presentation.model.SpotItem
import com.google.android.gms.maps.model.LatLng

fun List<Spot>.toSpotItems() =
    map {
        SpotItem(
            LatLng(
                it.lat,
                it.lng
            )
        )
    }