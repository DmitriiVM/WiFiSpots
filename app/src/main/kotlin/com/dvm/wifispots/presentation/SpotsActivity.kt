package com.dvm.wifispots.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.dvm.wifispots.R
import com.dvm.wifispots.databinding.ActivityMapsBinding
import com.dvm.wifispots.presentation.mappers.toSpotItems
import com.dvm.wifispots.presentation.model.SpotItem
import com.dvm.wifispots.presentation.model.SpotsRegion
import com.dvm.wifispots.presentation.util.android.requestLocationPermission
import com.dvm.wifispots.presentation.util.map.PlaceRenderer
import com.dvm.wifispots.presentation.util.map.getExtendedRange
import com.dvm.wifispots.presentation.util.map.isInSpotsRegion
import com.dvm.wifispots.presentation.util.map.moveToCurrentLocation
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.awaitMapLoad
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SpotsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityMapsBinding
    private lateinit var googleMap: GoogleMap

    private val viewModel: SpotsViewModel by viewModel()

    private var spotsRegion = SpotsRegion()
    private var hasVisibleSpots = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.findFragmentById(R.id.map).apply {
            this as SupportMapFragment
            getMapAsync(this@SpotsActivity)
        }

        requestLocationPermission {
            lifecycleScope.launch {
                googleMap.awaitMapLoad()
                googleMap.moveToCurrentLocation(baseContext)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap

        val clusterManager = ClusterManager<SpotItem>(this, googleMap)
        val placeRenderer = PlaceRenderer(this, googleMap, clusterManager)
        clusterManager.renderer = placeRenderer

        googleMap.setOnCameraIdleListener {
            onCameraIdle(googleMap, clusterManager)
        }

        viewModel.loading
            .onEach { binding.progress.isVisible = it }
            .launchIn(lifecycleScope)

        viewModel.spots
            .onEach { spots ->
                with(clusterManager) {
                    clearItems()
                    addItems(spots.toSpotItems())
                    cluster()
                }
                hasVisibleSpots = true
                binding.info.isVisible = false
            }
            .launchIn(lifecycleScope)
    }

    private fun onCameraIdle(
        googleMap: GoogleMap,
        clusterManager: ClusterManager<SpotItem>
    ) {
        if (googleMap.cameraPosition.zoom < VISIBILITY_ZOOM_BOUNDARY) {
            viewModel.cancelLoading()
            binding.info.isVisible = true
            if (hasVisibleSpots) {
                clusterManager.clearItems()
                clusterManager.cluster()
                hasVisibleSpots = false
            }
            return
        }

        clusterManager.onCameraIdle()

        val visibleRegion = googleMap.projection.visibleRegion
        val isInSpotsRegion = isInSpotsRegion(visibleRegion, spotsRegion)
        val spots = viewModel.spots.value

        if (isInSpotsRegion && spots.isNotEmpty() && !hasVisibleSpots) {
            clusterManager.addItems(spots.toSpotItems())
            clusterManager.cluster()
            hasVisibleSpots = true
            binding.info.isVisible = false
        }

        if (!isInSpotsRegion || spots.isEmpty()) {
            val (latRange, lngRange) = getExtendedRange(visibleRegion)
            spotsRegion = SpotsRegion(latRange, lngRange)
            viewModel.getSpots(latRange, lngRange)
            binding.info.isVisible = false
        }
    }

    companion object {
        private const val VISIBILITY_ZOOM_BOUNDARY = 14
    }
}