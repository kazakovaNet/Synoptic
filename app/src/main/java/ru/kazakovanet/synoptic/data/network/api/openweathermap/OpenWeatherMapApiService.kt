package ru.kazakovanet.synoptic.data.network.api.openweathermap

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kazakovanet.synoptic.data.network.ConnectivityInterceptor
import ru.kazakovanet.synoptic.data.network.response.future.FutureWeatherResponse

/**
 * Created by NKazakova on 02.07.2020.
 */

const val OPEN_WEATHER_MAP_APP_ID = "acf48084f5ac6eaf0058e32f1c004e6f"
const val EXCLUDE_QUERY = "current,minutely,hourly"
// https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&exclude=current,minutely,hourly&appid=acf48084f5ac6eaf0058e32f1c004e6f

interface OpenWeatherMapApiService {

    @GET("onecall")
    fun getFutureWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double
    ): Deferred<FutureWeatherResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): OpenWeatherMapApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter(
                        "exclude",
                        EXCLUDE_QUERY
                    )
                    .addQueryParameter(
                        "appid",
                        OPEN_WEATHER_MAP_APP_ID
                    )
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(OpenWeatherMapApiService::class.java)
        }
    }
}