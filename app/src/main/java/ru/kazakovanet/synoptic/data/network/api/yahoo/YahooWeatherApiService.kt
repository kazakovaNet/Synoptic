package ru.kazakovanet.synoptic.data.network.api.yahoo

import android.util.Base64
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import ru.kazakovanet.synoptic.data.network.ConnectivityInterceptor
import ru.kazakovanet.synoptic.data.network.api.AUTH_URL
import ru.kazakovanet.synoptic.data.network.api.CLIENT_ID
import ru.kazakovanet.synoptic.data.network.api.CLIENT_SECRET
import ru.kazakovanet.synoptic.data.network.api.model.AccessToken

/**
 * Created by NKazakova on 11.07.2020.
 */

interface YahooWeatherApiService {

    @FormUrlEncoded
    @POST("get_token")
    fun getAccessToken(
        @FieldMap fields: Map<String, String>
    ): Deferred<AccessToken>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): YahooWeatherApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .build()

                val base64Encoded =
                    Base64.encodeToString("$CLIENT_ID:$CLIENT_SECRET".toByteArray(), Base64.NO_WRAP)
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .header("Accept", "application/json")
                    .header("Authorization", "Basic $base64Encoded")
//                    .header("Content-Type", "application/x-www-form-urlencoded")
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
                .baseUrl(AUTH_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(YahooWeatherApiService::class.java)
        }
    }
}