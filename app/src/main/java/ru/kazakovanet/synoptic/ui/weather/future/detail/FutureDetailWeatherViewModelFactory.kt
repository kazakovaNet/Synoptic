package ru.kazakovanet.synoptic.ui.weather.future.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.repository.weather.future.FutureWeatherRepository

/**
 * Created by NKazakova on 09.07.2020.
 */
class FutureDetailWeatherViewModelFactory(
    private val detailDate: Long,
    private val repository: FutureWeatherRepository,
    private val unitProvider: UnitProvider
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureDetailWeatherViewModel(detailDate, repository, unitProvider) as T
    }
}