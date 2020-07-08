package ru.kazakovanet.synoptic.ui.weather.future.list

import androidx.lifecycle.ViewModel
import org.threeten.bp.LocalDate
import ru.kazakovanet.synoptic.data.repository.future.FutureWeatherRepository
import ru.kazakovanet.synoptic.internal.lazyDeferred

class FutureListWeatherViewModel(
    repository: FutureWeatherRepository
) : ViewModel() {

    val weatherEntries by lazyDeferred {
        repository.getFutureWeatherList(LocalDate.now())
    }

    val weatherLocation by lazyDeferred {
        repository.getFutureWeatherLocation()
    }
}