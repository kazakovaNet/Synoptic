package ru.kazakovanet.synoptic.ui.weather.future.list

import androidx.lifecycle.ViewModel
import org.threeten.bp.LocalDate
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.repository.weather.future.FutureWeatherRepository
import ru.kazakovanet.synoptic.internal.UnitSystem
import ru.kazakovanet.synoptic.internal.lazyDeferred
import java.util.*

class FutureListWeatherViewModel(
    repository: FutureWeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weatherEntries by lazyDeferred {
        repository.getFutureWeatherList(
            LocalDate.now(),
            unitSystem.name.toLowerCase(Locale.getDefault())
        )
    }

    val weatherLocation by lazyDeferred {
        repository.getFutureWeatherLocation()
    }
}