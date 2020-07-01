package ru.kazakovanet.synoptic.data.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import ru.kazakovanet.synoptic.data.db.CurrentWeatherDao
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherEntry
import ru.kazakovanet.synoptic.data.network.WeatherNetworkDataSource
import ru.kazakovanet.synoptic.data.network.response.CurrentWeatherResponse

class SynopticRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource
) : SynopticRepository {

    init {
        weatherNetworkDataSource.downloadedCurrentWeather.observeForever { newCurrentWeather ->
            persistFetchedCurrentWeather(newCurrentWeather)
        }
    }

    override suspend fun getCurrentWeather(unitSystem: String): LiveData<out CurrentWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData(unitSystem)
            currentWeatherDao.getWeather()
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: CurrentWeatherResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.currentWeatherEntry)
        }
    }

    private suspend fun initWeatherData(unitSystem: String) {
        // todo
        if (isFetchCurrentNeeded(ZonedDateTime.now().minusHours(1)))
            fetchCurrentWeather(unitSystem)
    }

    private suspend fun fetchCurrentWeather(unitSystem: String) {
        // todo
        weatherNetworkDataSource.fetchCurrentWeather("London", unitSystem)
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}