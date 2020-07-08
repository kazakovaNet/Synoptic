package ru.kazakovanet.synoptic.data.repository.future

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZonedDateTime
import ru.kazakovanet.synoptic.data.db.FutureWeatherDao
import ru.kazakovanet.synoptic.data.db.FutureWeatherLocationDao
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherEntry
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherLocation
import ru.kazakovanet.synoptic.data.network.datasource.future.FORECAST_DAYS_COUNT
import ru.kazakovanet.synoptic.data.network.datasource.future.FutureWeatherNetworkDataSource
import ru.kazakovanet.synoptic.data.network.response.future.FutureWeatherResponse
import ru.kazakovanet.synoptic.data.provider.LocationProvider

class FutureWeatherRepositoryImpl(
    private val futureWeatherDao: FutureWeatherDao,
    private val futureWeatherLocationDao: FutureWeatherLocationDao,
    private val dataSource: FutureWeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : FutureWeatherRepository {

    init {
        dataSource.downloadedFutureWeather.observeForever { newFutureWeather ->
            persistFetchedFutureWeather(newFutureWeather)
        }
    }

    override suspend fun getFutureWeatherList(startDate: LocalDate): LiveData<out List<FutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            futureWeatherDao.getWeatherForecast(startDate.toEpochDay())
        }
    }

    private fun persistFetchedFutureWeather(fetchedWeather: FutureWeatherResponse) {

        fun deleteOldForecastData() {
            val today = LocalDate.now().toEpochDay()
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            futureWeatherDao.upsert(fetchedWeather.futureWeatherEntries)
            futureWeatherLocationDao.upsert(fetchedWeather.futureWeatherLocation)
        }
    }

    private suspend fun initWeatherData() {
        val location = futureWeatherLocationDao.getLocationNonLive()

        if (location == null || locationProvider.hasLocationChanged(location)) {
            fetchFutureWeather()
            return
        }

        if (isFetchFutureNeeded(location.zonedDateTime))
            fetchFutureWeather()
    }

    private suspend fun fetchFutureWeather() {
        // TODO: 07.07.2020
        val lat = 33.44
        val lon = -94.04
        dataSource.fetchFutureWeather(lat, lon)
    }

    private fun isFetchFutureNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val today = LocalDate.now().toEpochDay()
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }

    override suspend fun getFutureWeatherLocation(): LiveData<FutureWeatherLocation> {
        TODO("Not yet implemented")
    }
}