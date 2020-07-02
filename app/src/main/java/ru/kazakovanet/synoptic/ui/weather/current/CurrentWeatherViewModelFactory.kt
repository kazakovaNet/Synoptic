package ru.kazakovanet.synoptic.ui.weather.current

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.repository.SynopticRepository

/**
 * Created by NKazakova on 30.06.2020.
 */
class CurrentWeatherViewModelFactory(
    private val synopticRepository: SynopticRepository,
    private val unitProvider: UnitProvider
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(synopticRepository, unitProvider) as T
    }
}