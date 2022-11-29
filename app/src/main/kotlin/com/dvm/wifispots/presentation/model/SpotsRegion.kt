package com.dvm.wifispots.presentation.model

import android.util.Range

data class SpotsRegion(
    val latRange: Range<Double> = Range(0.0,0.0),
    val lngRange: Range<Double> = Range(0.0,0.0),
)