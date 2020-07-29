package ru.kazakovanet.synoptic.data.repository.weather

import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import ru.kazakovanet.synoptic.data.datamapper.CurrentWeatherDataMapper
import ru.kazakovanet.synoptic.data.datamapper.ForecastDataMapper
import ru.kazakovanet.synoptic.data.datamapper.LocationDataMapper
import ru.kazakovanet.synoptic.data.db.dao.weather.CurrentWeatherDao
import ru.kazakovanet.synoptic.data.db.dao.weather.ForecastWeatherDao
import ru.kazakovanet.synoptic.data.db.dao.weather.LocationDao
import ru.kazakovanet.synoptic.data.db.entity.ForecastWeatherEntity
import ru.kazakovanet.synoptic.data.db.entity.LocationEntity
import ru.kazakovanet.synoptic.data.db.entity.WeatherEntity
import ru.kazakovanet.synoptic.data.network.api.dto.weather.ForecastDTO
import ru.kazakovanet.synoptic.data.network.api.dto.weather.WeatherResponseDTO
import ru.kazakovanet.synoptic.data.network.datasource.weather.WeatherNetworkDataSource
import ru.kazakovanet.synoptic.data.provider.LocationProvider

class WeatherRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val forecastDao: ForecastWeatherDao,
    private val locationDao: LocationDao,
    private val dataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider
) : WeatherRepository {
    init {
        dataSource.downloadedWeather.observeForever { newWeather ->
            persistFetchedWeather(newWeather)
        }
    }

    override suspend fun getCurrentWeather(unitSystem: String): LiveData<out WeatherEntity> {
        return withContext(Dispatchers.IO) {
            initWeatherData(unitSystem)
            currentWeatherDao.getWeather()
        }
    }

    override suspend fun getForecastList(
        startDate: Long,
        unitSystem: String
    ): LiveData<out List<ForecastWeatherEntity>> {
        return withContext(Dispatchers.IO) {
            initWeatherData(unitSystem)
            forecastDao.getWeatherForecast(startDate)
        }
    }

    override suspend fun getWeatherByDate(
        date: Long,
        unitSystem: String
    ): LiveData<out ForecastWeatherEntity> {
        return withContext(Dispatchers.IO) {
            initWeatherData(unitSystem)
            forecastDao.getDetailWeatherByDay(date)
        }
    }

    override suspend fun getWeatherLocation(): LiveData<LocationEntity> {
        return withContext(Dispatchers.IO) {
            locationDao.getLocation()
        }
    }

    private fun persistFetchedWeather(fetchedWeather: WeatherResponseDTO) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(
                CurrentWeatherDataMapper.map(fetchedWeather.currentObservation)
            )
            locationDao.upsert(LocationDataMapper.map(fetchedWeather.location))
            persistFetchedForecast(fetchedWeather.forecasts)
        }
    }

    private fun persistFetchedForecast(forecasts: List<ForecastDTO>) {
        fun deleteOldForecastData() {
            forecastDao.deleteOldEntries(System.currentTimeMillis() / 1000)
        }

        GlobalScope.launch(Dispatchers.IO) {
            deleteOldForecastData()
            forecastDao.upsert(ForecastDataMapper.map(forecasts))
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