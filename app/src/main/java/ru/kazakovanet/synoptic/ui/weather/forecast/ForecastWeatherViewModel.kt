package ru.kazakovanet.synoptic.ui.weather.forecast

import androidx.lifecycle.ViewModel
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.repository.weather.WeatherRepository
import ru.kazakovanet.synoptic.internal.UnitSystem
import ru.kazakovanet.synoptic.internal.lazyDeferred
import java.util.*

class ForecastWeatherViewModel(
    repository: WeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weatherEntries by lazyDeferred {
        repository.getForecastList(
            System.currentTimeMillis() / 1000,
            unitSystem.name.toLowerCase(Locale.getDefault())
        )
    }

    val weatherLocation by lazyDeferred {
        repository.getWeatherLocation()
    }
}