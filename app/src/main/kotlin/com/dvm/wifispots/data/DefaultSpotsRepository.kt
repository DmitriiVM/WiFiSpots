package com.dvm.wifispots.data

import android.content.Context
import android.util.Range
import com.dvm.wifispots.R
import com.dvm.wifispots.data.model.Spot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader

class DefaultSpotsRepository(private val context: Context) : SpotsRepository {

    private var stream: BufferedReader? = null

    override suspend fun getSpots(
        latRange: Range<Double>,
        lngRange: Range<Double>
    ): List<Spot> = withContext(Dispatchers.IO) {
        stream?.close()
        stream = context.resources
            .openRawResource(SPOTS_RESOURCE)
            .bufferedReader()

        stream.use { reader ->
            reader ?: return@use emptyList()
            try {
                val header = reader.readLine()
                reader.lineSequence()
                    .map { parseSpotInRange(it, latRange, lngRange) }
                    .filterNotNull()
                    .toList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override fun closeStream() {
        stream?.close()
        stream = null
    }

    private fun parseSpotInRange(
        line: String,
        latRange: Range<Double>,
        lngRange: Range<Double>
    ): Spot? {
        val data = line.split(',')
        val lat = data[2].toDoubleOrNull() ?: return null
        if (lat !in latRange) {
            return null
        }
        val lng = data[3].toDoubleOrNull() ?: return null
        if (lng !in lngRange) {
            return null
        }
        return Spot(lat, lng)
    }

    companion object {
        private const val SPOTS_RESOURCE = R.raw.hotspots
    }
}