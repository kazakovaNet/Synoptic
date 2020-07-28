package ru.kazakovanet.synoptic.data.repository.weather

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import ru.kazakovanet.synoptic.data.datamapper.YahooCurrentWeatherDataMapper
import ru.kazakovanet.synoptic.data.datamapper.YahooForecastDataMapper
import ru.kazakovanet.synoptic.data.datamapper.YahooLocationDataMapper
import ru.kazakovanet.synoptic.data.db.dao.weather.YahooCurrentWeatherDao
import ru.kazakovanet.synoptic.data.db.dao.weather.YahooForecastDao
import ru.kazakovanet.synoptic.data.db.dao.weather.YahooLocationDao
import ru.kazakovanet.synoptic.data.db.entity.YahooForecastEntity
import ru.kazakovanet.synoptic.data.db.entity.YahooLocationEntity
import ru.kazakovanet.synoptic.data.db.entity.YahooWeatherEntity
import ru.kazakovanet.synoptic.data.network.api.dto.weather.ForecastDTO
import ru.kazakovanet.synoptic.data.network.api.dto.weather.YahooWeatherResponseDTO
import ru.kazakovanet.synoptic.data.network.datasource.weather.YahooWeatherNetworkDataSource
import ru.kazakovanet.synoptic.data.provider.LocationProvider

class YahooWeatherRepositoryImpl(
    private val currentWeatherDao: YahooCurrentWeatherDao,
    private val forecastDao: YahooForecastDao,
    private val locationDao: YahooLocationDao,
    private val dataSource: YahooWeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : YahooWeatherRepository {
    init {
        dataSource.downloadedWeather.observeForever { newWeather ->
            persistFetchedWeather(newWeather)
        }
    }

    override suspend fun getCurrentWeather(unitSystem: String): LiveData<out YahooWeatherEntity> {
        return withContext(Dispatchers.IO) {
            initWeatherData(unitSystem)
            currentWeatherDao.getWeather()
        }
    }

    override suspend fun getForecastList(
        startDate: Long,
        unitSystem: String
    ): LiveData<out List<YahooForecastEntity>> {
        return withContext(Dispatchers.IO) {
            initWeatherData(unitSystem)
            forecastDao.getWeatherForecast(startDate)
        }
    }

    override suspend fun getWeatherByDate(
        date: Long,
        unitSystem: String
    ): LiveData<out YahooForecastEntity> {
        return withContext(Dispatchers.IO) {
            initWeatherData(unitSystem)
            forecastDao.getDetailWeatherByDay(date)
        }
    }

    override suspend fun getWeatherLocation(): LiveData<YahooLocationEntity> {
        return withContext(Dispatchers.IO) {
            locationDao.getLocation()
        }
    }

    private fun persistFetchedWeather(fetchedWeather: YahooWeatherResponseDTO) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(
                YahooCurrentWeatherDataMapper.map(fetchedWeather.currentObservation)
            )
            locationDao.upsert(YahooLocationDataMapper.map(fetchedWeather.location))
            persistFetchedForecast(fetchedWeather.forecasts)
        }
    }

    private fun persistFetchedForecast(forecasts: List<ForecastDTO>) {
        fun deleteOldForecastData() {
            forecastDao.deleteOldEntries(System.currentTimeMillis())
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            forecastDao.upsert(YahooForecastDataMapper.map(forecasts))
        }
    }

    private suspend fun initWeatherData(unitSystem: String) {
        val lastLocation = locationDao.getLocationNonLive()

        if (lastLocation == null
            || locationProvider.hasLocationChanged(lastLocation)
        ) {
            fetchWeather(unitSystem)
            return
        }

        if (isFetchNeeded(lastLocation.zonedDateTime))
            fetchWeather(unitSystem)
    }

    private suspend fun fetchWeather(unitSystem: String) {
        dataSource.fetchWeather(
            locationProvider.getPreferredLocationString(), unitSystem
        )
    }

    private fun isFetchNeeded(lastFetchTime: ZonedDateTime): Boolean {
        // TODO: 28.07.2020 change unit format
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }
}