package ru.kazakovanet.synoptic.ui.weather.current

import androidx.lifecycle.ViewModel
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.repository.auth.AuthApiRepository
import ru.kazakovanet.synoptic.data.repository.weather.WeatherRepository
import ru.kazakovanet.synoptic.internal.UnitSystem
import ru.kazakovanet.synoptic.internal.lazyDeferred

class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    private val authApiRepository: AuthApiRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetric: Boolean
        get() = unitSystem == UnitSystem.METRIC

    val weather by lazyDeferred {
        weatherRepository.getCurrentWeather(unitSystem.designation)
    }

    val weatherLocation by lazyDeferred {
        weatherRepository.getWeatherLocation()
    }

    suspend fun isAccessTokenReceived(): Boolean {
        return authApiRepository.isAccessTokenReceived()
    }
}