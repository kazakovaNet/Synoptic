package ru.kazakovanet.synoptic.data.repository.weather

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.db.entity.ForecastWeatherEntity
import ru.kazakovanet.synoptic.data.db.entity.LocationEntity
import ru.kazakovanet.synoptic.data.db.entity.WeatherEntity

/**
 * Created by NKazakova on 22.07.2020.
 */
interface WeatherRepository {
    suspend fun getCurrentWeather(unitSystem: String): LiveData<out WeatherEntity>
    suspend fun getForecastList(
        startDate: Long,
        unitSystem: String
    ): LiveData<out List<ForecastWeatherEntity>>

    suspend fun getWeatherByDate(date: Long, unitSystem: String): LiveData<out ForecastWeatherEntity>

    suspend fun getWeatherLocation(): LiveData<LocationEntity>
}