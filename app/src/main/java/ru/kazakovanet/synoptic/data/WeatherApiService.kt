package ru.kazakovanet.synoptic.data

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kazakovanet.synoptic.data.response.CurrentWeatherResponse

/**
 * Created by NKazakova on 26.06.2020.
 */

const val API_KEY = "52b43b94b77995280641d32ad8b5d21e"
// http://api.weatherstack.com/current?access_key=52b43b94b77995280641d32ad8b5d21e&query=New%20York

interface WeatherApiService {

    @GET("current")
    fun getCurrentWeather(
        @Query("query") location: String
//        @Query("language") language: String = "ru"
    ): Deferred<CurrentWeatherResponse>

    companion object {
        operator fun invoke(): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key", API_KEY)
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.weatherstack.com/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}