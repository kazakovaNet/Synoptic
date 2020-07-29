package ru.kazakovanet.synoptic.data.network.api.service.weather

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import ru.kazakovanet.synoptic.data.db.dao.auth.AccessTokenDao
import ru.kazakovanet.synoptic.data.network.ConnectivityInterceptor
import ru.kazakovanet.synoptic.data.network.api.BASE_WEATHER_URL
import ru.kazakovanet.synoptic.data.network.api.dto.weather.WeatherResponseDTO
import ru.kazakovanet.synoptic.internal.AccessTokenNotFoundException

/**
 * Created by NKazakova on 11.07.2020.
 */
const val FORMAT_JSON = "json"

interface WeatherApiService {

    @GET("forecastrss")
    fun getWeatherByLocation(
        @Query("location") location: String,
        @Query("u") units: String
    ): Deferred<WeatherResponseDTO>

    @GET("forecastrss")
    fun getWeatherByLatLon(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("u") units: String
    ): Deferred<WeatherResponseDTO>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor,
            accessTokenDao: AccessTokenDao
        ): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter(
                        "format",
                        FORMAT_JSON
                    )
                    .build()

                val accessToken =
                    accessTokenDao.getAccessTokenNonLive() ?: throw AccessTokenNotFoundException()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .header("Authorization", "Bearer ${accessToken.accessToken}")
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .addInterceptor(logging)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(BASE_WEATHER_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}