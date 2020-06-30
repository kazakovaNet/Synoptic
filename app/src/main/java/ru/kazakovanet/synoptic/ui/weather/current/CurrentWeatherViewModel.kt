package ru.kazakovanet.synoptic.ui.weather.current

import androidx.lifecycle.ViewModel
import ru.kazakovanet.synoptic.data.repository.SynopticRepository
import ru.kazakovanet.synoptic.internal.lazyDeferred

class CurrentWeatherViewModel(private val repository: SynopticRepository) : ViewModel() {

    val weather by lazyDeferred {
        repository.getCurrentWeather()
    }
}