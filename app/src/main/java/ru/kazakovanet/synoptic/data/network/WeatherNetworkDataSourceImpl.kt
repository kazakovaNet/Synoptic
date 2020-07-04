package ru.kazakovanet.synoptic.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kazakovanet.synoptic.data.network.api.openweathermap.OpenWeatherMapApiService
import ru.kazakovanet.synoptic.data.network.api.weatherstack.WeatherStackApiService
import ru.kazakovanet.synoptic.data.network.response.CurrentWeatherResponse
import ru.kazakovanet.synoptic.data.network.response.FutureWeatherResponse
import ru.kazakovanet.synoptic.internal.NoConnectivityException

class WeatherNetworkDataSourceImpl(
    private val weatherStackApiService: WeatherStackApiService,
    private val openWeatherMapApiService: OpenWeatherMapApiService
) : WeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

    override suspend fun fetchCurrentWeather(location: String, units: String) {
        try {
            val fetchedCurrentWeather =
                weatherStackApiService.getCurrentWeather(location, units)
                    .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }

    override suspend fun fetchFutureWeather(lat: Double, lon: Double) {
        try {
            val fetchedFutureWeather =
                openWeatherMapApiService.getFutureWeather(lat, lon)
                    .await()
            _downloadedFutureWeather.postValue(fetchedFutureWeather)
        } catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection", e)
        }
    }
}