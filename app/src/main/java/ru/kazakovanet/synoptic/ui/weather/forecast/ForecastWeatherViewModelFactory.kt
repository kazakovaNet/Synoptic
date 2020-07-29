package ru.kazakovanet.synoptic.ui.weather.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.repository.weather.WeatherRepository

/**
 * Created by NKazakova on 08.07.2020.
 */
class ForecastWeatherViewModelFactory(
    private val repository: WeatherRepository,
    private val unitProvider: UnitProvider
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ForecastWeatherViewModel(
            repository,
            unitProvider
        ) as T
    }
}