package com.dvm.wifispots.presentation.util.map

import android.content.Context
import androidx.core.content.ContextCompat
import com.dvm.wifispots.R
import com.dvm.wifispots.presentation.model.SpotItem
import com.dvm.wifispots.presentation.util.android.BitmapHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class PlaceRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<SpotItem>
) : DefaultClusterRenderer<SpotItem>(context, map, clusterManager) {

    override fun onBeforeClusterItemRendered(
        item: SpotItem,
        markerOptions: MarkerOptions
    ) {
        val icon = BitmapHelper.vectorToBitmap(
            context = context,
            id = com.google.android.material.R.drawable.btn_radio_on_mtrl,
            color = ContextCompat.getColor(context, R.color.purple_700)
        )
        markerOptions
            .position(item.latLng)
            .icon(icon)
    }

    override fun onClusterItemRendered(clusterItem: SpotItem, marker: Marker) {
        marker.tag = clusterItem
    }
}