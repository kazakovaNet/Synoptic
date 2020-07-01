package ru.kazakovanet.synoptic.ui.weather.current

import androidx.lifecycle.ViewModel
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.repository.SynopticRepository
import ru.kazakovanet.synoptic.internal.UnitSystem
import ru.kazakovanet.synoptic.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val synopticRepository: SynopticRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        synopticRepository.getCurrentWeather(unitSystem.designation)
    }

    val weatherLocation by lazyDeferred {
        synopticRepository.getWeatherLocation()
    }
}