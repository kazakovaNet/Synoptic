package ru.kazakovanet.synoptic.ui.weather.future.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.kazakovanet.synoptic.data.repository.future.FutureWeatherRepository

/**
 * Created by NKazakova on 08.07.2020.
 */
class FutureListWeatherViewModelFactory(
    private val repository: FutureWeatherRepository
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FutureListWeatherViewModel(repository) as T
    }
}