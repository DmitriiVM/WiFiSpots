package com.dvm.wifispots.data

import android.util.Range
import com.dvm.wifispots.data.model.Spot

interface SpotsRepository {

    suspend fun getSpots(
        latRange: Range<Double>,
        lngRange: Range<Double>
    ): List<Spot>

    fun closeStream()
}