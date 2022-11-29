package com.dvm.wifispots.presentation

import android.util.Range
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dvm.wifispots.data.SpotsRepository
import com.dvm.wifispots.data.model.Spot
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SpotsViewModel(private val dataSource: SpotsRepository) : ViewModel() {

    private val _spots = MutableStateFlow(emptyList<Spot>())
    val spots = _spots.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading = _loading.asStateFlow()

    fun getSpots(
        latRange: Range<Double>,
        lngRange: Range<Double>
    ) {
        _loading.update { true }
        viewModelScope.coroutineContext.cancelChildren()
        viewModelScope.launch {
            _spots.update {
                dataSource.getSpots(latRange, lngRange)
            }
            _loading.update { false }
        }
    }

    fun cancelLoading() {
        _loading.update { false }
        dataSource.closeStream()
    }
}