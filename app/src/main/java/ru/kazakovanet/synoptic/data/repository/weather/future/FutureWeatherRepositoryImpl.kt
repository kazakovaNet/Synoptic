package ru.kazakovanet.synoptic.data.repository.weather.future

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import ru.kazakovanet.synoptic.data.db.dao.weather.future.FutureWeatherDao
import ru.kazakovanet.synoptic.data.db.dao.weather.future.FutureWeatherLocationDao
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherEntry
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherLocation
import ru.kazakovanet.synoptic.data.network.datasource.weather.future.FORECAST_DAYS_COUNT
import ru.kazakovanet.synoptic.data.network.datasource.weather.future.FutureWeatherNetworkDataSource
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

    override suspend fun getFutureWeatherList(
        startDate: LocalDate,
        unitSystem: String
    ): LiveData<out List<FutureWeatherEntry>> {
        return withContext(Dispatchers.IO) {
            initWeatherData(unitSystem)
            // TODO: 08.07.2020  
            val epochSecond = getEpochSecond(startDate, "America/Chicago")
            futureWeatherDao.getWeatherForecast(epochSecond)
        }
    }

    override suspend fun getFutureWeatherByDate(
        date: Long,
        unitSystem: String
    ): LiveData<out FutureWeatherEntry> {
        return withContext(Dispatchers.IO) {
            initWeatherData(unitSystem)
            futureWeatherDao.getDetailWeatherByDay(date)
        }
    }

    private fun persistFetchedFutureWeather(fetchedWeather: FutureWeatherResponse) {

        fun deleteOldForecastData() {
            val today =
                getEpochSecond(LocalDate.now(), fetchedWeather.futureWeatherLocation.timezoneId)
            futureWeatherDao.deleteOldEntries(today)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            futureWeatherDao.upsert(fetchedWeather.futureWeatherEntries)
            futureWeatherLocationDao.upsert(fetchedWeather.futureWeatherLocation)
        }
    }

    private suspend fun initWeatherData(unitSystem: String) {
        val location = futureWeatherLocationDao.getLocationNonLive()

        if (location == null /*|| locationProvider.hasLocationChanged(location)*/) {
            fetchFutureWeather(unitSystem)
            return
        }

        if (isFetchFutureNeeded(location.timezoneId))
            fetchFutureWeather(unitSystem)
    }

    private suspend fun fetchFutureWeather(unitSystem: String) {
        // TODO: 07.07.2020
        val lat = 33.44
        val lon = -94.04
        dataSource.fetchFutureWeather(lat, lon, unitSystem)
    }

    private fun isFetchFutureNeeded(timezoneId: String): Boolean {
        val today = getEpochSecond(LocalDate.now(), timezoneId)
        val futureWeatherCount = futureWeatherDao.countFutureWeather(today)
        return futureWeatherCount < FORECAST_DAYS_COUNT
    }

    private fun getEpochSecond(localDate: LocalDate, timezoneId: String): Long {
        val zoneId = ZoneId.of(timezoneId)
        return localDate.atStartOfDay(zoneId).toEpochSecond()
    }

    override suspend fun getFutureWeatherLocation(): LiveData<FutureWeatherLocation> {
        return withContext(Dispatchers.IO) {
            futureWeatherLocationDao.getLocation()
        }
    }
}