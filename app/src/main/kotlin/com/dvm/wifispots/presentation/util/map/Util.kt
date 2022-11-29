package com.dvm.wifispots.presentation.util.map

import android.util.Range
import com.dvm.wifispots.presentation.model.SpotsRegion
import com.google.android.gms.maps.model.VisibleRegion

fun isInSpotsRegion(
    visibleRegion: VisibleRegion,
    spotsRegion: SpotsRegion
): Boolean =
    spotsRegion.latRange.lower < visibleRegion.nearLeft.latitude &&
            spotsRegion.latRange.upper > visibleRegion.farLeft.latitude &&
            spotsRegion.lngRange.lower < visibleRegion.nearLeft.longitude &&
            spotsRegion.lngRange.upper > visibleRegion.nearRight.longitude

fun getExtendedRange(
    visibleRegion: VisibleRegion
): Pair<Range<Double>, Range<Double>> {
    val latRange = Range(
        visibleRegion.nearLeft.latitude - EXTENDED_DISTANCE,
        visibleRegion.farLeft.latitude + EXTENDED_DISTANCE
    )
    val lngRange = Range(
        visibleRegion.nearLeft.longitude - EXTENDED_DISTANCE,
        visibleRegion.nearRight.longitude + EXTENDED_DISTANCE
    )
    return Pair(latRange, lngRange)
}

private const val EXTENDED_DISTANCE = 0.05