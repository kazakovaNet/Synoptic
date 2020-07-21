package ru.kazakovanet.synoptic.ui.weather.current

import androidx.lifecycle.ViewModel
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.repository.auth.YahooAuthApiRepository
import ru.kazakovanet.synoptic.data.repository.weather.current.CurrentWeatherRepository
import ru.kazakovanet.synoptic.internal.UnitSystem
import ru.kazakovanet.synoptic.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val authApiRepository: YahooAuthApiRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        currentWeatherRepository.getCurrentWeather(unitSystem.designation)
    }

    val weatherLocation by lazyDeferred {
        currentWeatherRepository.getWeatherLocation()
    }

    suspend fun isAccessTokenReceived(): Boolean {
        return authApiRepository.isAccessTokenReceived()
    }
}