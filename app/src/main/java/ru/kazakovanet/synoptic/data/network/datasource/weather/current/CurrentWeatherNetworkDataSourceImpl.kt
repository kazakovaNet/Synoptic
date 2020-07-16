package ru.kazakovanet.synoptic.data.network.datasource.weather.current

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kazakovanet.synoptic.data.network.api.weatherstack.WeatherStackApiService
import ru.kazakovanet.synoptic.data.network.response.current.CurrentWeatherResponse
import ru.kazakovanet.synoptic.internal.NoConnectivityException

class CurrentWeatherNetworkDataSourceImpl(
    private val weatherStackApiService: WeatherStackApiService
) : CurrentWeatherNetworkDataSource {

    private val _downloadedCurrentWeather = MutableLiveData<CurrentWeatherResponse>()
    override val downloadedCurrentWeather: LiveData<CurrentWeatherResponse>
        get() = _downloadedCurrentWeather

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
}