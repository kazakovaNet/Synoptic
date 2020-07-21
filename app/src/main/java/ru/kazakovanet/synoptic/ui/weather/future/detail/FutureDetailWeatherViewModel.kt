package ru.kazakovanet.synoptic.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.repository.weather.future.FutureWeatherRepository
import ru.kazakovanet.synoptic.internal.UnitSystem
import ru.kazakovanet.synoptic.internal.lazyDeferred
import java.util.*

class FutureDetailWeatherViewModel(
    private val detailDate: Long,
    private val repository: FutureWeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() { // TODO: 09.07.2020 выделить родительский класс 

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        repository.getFutureWeatherByDate(
            detailDate,
            unitSystem.name.toLowerCase(Locale.getDefault())
        )
    }

    val weatherLocation by lazyDeferred {
        repository.getFutureWeatherLocation()
    }
}