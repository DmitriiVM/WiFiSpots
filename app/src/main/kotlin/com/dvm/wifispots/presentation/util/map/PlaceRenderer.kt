package com.dvm.wifispots.presentation.util.map

import android.content.Context
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import androidx.core.content.ContextCompat
import com.dvm.wifispots.R
import com.dvm.wifispots.presentation.model.SpotItem
import com.dvm.wifispots.presentation.util.android.BitmapHelper
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class PlaceRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<SpotItem>
) : DefaultClusterRenderer<SpotItem>(context, map, clusterManager) {

    override fun getDescriptorForCluster(cluster: Cluster<SpotItem>): BitmapDescriptor {
        val drawable = ShapeDrawable(OvalShape())
        val colorRes: Int
        when (cluster.size) {
            in 2..9 -> {
                drawable.intrinsicHeight = 60
                drawable.intrinsicWidth = 60
                colorRes = R.color.purple_700
            }
            in 10..29 -> {
                drawable.intrinsicHeight = 75
                drawable.intrinsicWidth = 75
                colorRes = R.color.blue_700
            }
            in 30..99 -> {
                drawable.intrinsicHeight = 90
                drawable.intrinsicWidth = 90
                colorRes = R.color.blue_1_700
            }
            else -> {
                drawable.intrinsicHeight = 105
                drawable.intrinsicWidth = 105
                colorRes = R.color.teal_700
            }
        }

        return BitmapHelper.drawableToBitmap(
            drawable = drawable,
            color = ContextCompat.getColor(context, colorRes)
        )
    }

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