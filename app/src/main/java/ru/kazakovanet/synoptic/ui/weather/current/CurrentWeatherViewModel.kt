package ru.kazakovanet.synoptic.ui.weather.current

import androidx.lifecycle.ViewModel
import ru.kazakovanet.synoptic.data.repository.SynopticRepository
import ru.kazakovanet.synoptic.internal.UnitSystem
import ru.kazakovanet.synoptic.internal.lazyDeferred

class CurrentWeatherViewModel(private val repository: SynopticRepository) : ViewModel() {

    private val unitSystem = UnitSystem.METRIC //get from settings later

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        repository.getCurrentWeather()
    }
}