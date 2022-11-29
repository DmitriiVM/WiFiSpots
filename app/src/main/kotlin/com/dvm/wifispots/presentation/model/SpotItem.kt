package com.dvm.wifispots.presentation.model

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem

data class SpotItem(val latLng: LatLng) : ClusterItem {

    override fun getPosition(): LatLng = latLng

    override fun getTitle(): String = ""

    override fun getSnippet(): String = ""
}