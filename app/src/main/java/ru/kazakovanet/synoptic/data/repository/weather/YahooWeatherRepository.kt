package ru.kazakovanet.synoptic.data.repository.weather

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherLocation
import ru.kazakovanet.synoptic.data.db.entity.YahooForecastEntity
import ru.kazakovanet.synoptic.data.db.entity.YahooLocationEntity
import ru.kazakovanet.synoptic.data.db.entity.YahooWeatherEntity

/**
 * Created by NKazakova on 22.07.2020.
 */
interface YahooWeatherRepository {
    suspend fun getCurrentWeather(unitSystem: String): LiveData<out YahooWeatherEntity>
    suspend fun getForecastList(
        startDate: Long,
        unitSystem: String
    ): LiveData<out List<YahooForecastEntity>>

    suspend fun getWeatherByDate(date: Long, unitSystem: String): LiveData<out YahooForecastEntity>

    suspend fun getWeatherLocation(): LiveData<YahooLocationEntity>
}