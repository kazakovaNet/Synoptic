package ru.kazakovanet.synoptic.data.repository.weather.current

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherEntry
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherLocation

/**
 * Created by NKazakova on 27.06.2020.
 */

interface CurrentWeatherRepository {
    suspend fun getCurrentWeather(unitSystem: String): LiveData<out CurrentWeatherEntry>

    suspend fun getWeatherLocation(): LiveData<CurrentWeatherLocation>
}