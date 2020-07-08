package ru.kazakovanet.synoptic.data.network.datasource.future

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.kazakovanet.synoptic.data.network.api.openweathermap.OpenWeatherMapApiService
import ru.kazakovanet.synoptic.data.network.response.future.FutureWeatherResponse
import ru.kazakovanet.synoptic.internal.NoConnectivityException

const val FORECAST_DAYS_COUNT = 7

class FutureWeatherNetworkDataSourceImpl(
    private val openWeatherMapApiService: OpenWeatherMapApiService
) : FutureWeatherNetworkDataSource {

    private val _downloadedFutureWeather = MutableLiveData<FutureWeatherResponse>()
    override val downloadedFutureWeather: LiveData<FutureWeatherResponse>
        get() = _downloadedFutureWeather

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