package ru.kazakovanet.synoptic

import android.app.Application
import android.content.Context
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import ru.kazakovanet.synoptic.data.db.SynopticDatabase
import ru.kazakovanet.synoptic.data.network.ConnectivityInterceptor
import ru.kazakovanet.synoptic.data.network.ConnectivityInterceptorImpl
import ru.kazakovanet.synoptic.data.network.api.service.auth.AuthApiService
import ru.kazakovanet.synoptic.data.network.api.service.weather.WeatherApiService
import ru.kazakovanet.synoptic.data.network.datasource.auth.AuthNetworkDataSource
import ru.kazakovanet.synoptic.data.network.datasource.auth.AuthNetworkDataSourceImpl
import ru.kazakovanet.synoptic.data.network.datasource.weather.WeatherNetworkDataSource
import ru.kazakovanet.synoptic.data.network.datasource.weather.WeatherNetworkDataSourceImpl
import ru.kazakovanet.synoptic.data.provider.LocationProvider
import ru.kazakovanet.synoptic.data.provider.LocationProviderImpl
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.provider.UnitProviderImpl
import ru.kazakovanet.synoptic.data.repository.auth.AuthApiRepository
import ru.kazakovanet.synoptic.data.repository.auth.AuthApiRepositoryImpl
import ru.kazakovanet.synoptic.data.repository.weather.WeatherRepository
import ru.kazakovanet.synoptic.data.repository.weather.WeatherRepositoryImpl
import ru.kazakovanet.synoptic.ui.auth.AuthViewModelFactory
import ru.kazakovanet.synoptic.ui.weather.current.CurrentWeatherViewModelFactory
import ru.kazakovanet.synoptic.ui.weather.forecast.ForecastWeatherViewModelFactory

/**
 * Created by NKazakova on 27.06.2020.
 */

class SynopticApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@SynopticApplication))

        // Database
        bind<SynopticDatabase>() with singleton {
            Room.databaseBuilder(
                this@SynopticApplication,
                SynopticDatabase::class.java,
                "synoptic.db"
            ).build()
        }

        // DAO
        bind() from singleton { instance<SynopticDatabase>().accessTokenDao() }
        bind() from singleton { instance<SynopticDatabase>().currentWeatherDao() }
        bind() from singleton { instance<SynopticDatabase>().forecastWeatherDao() }
        bind() from singleton { instance<SynopticDatabase>().locationDao() }

        // Interceptor
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(context = instance()) }

        // ApiService
        bind() from singleton {
            AuthApiService(
                connectivityInterceptor = instance()
            )
        }
        bind() from singleton {
            WeatherApiService(
                connectivityInterceptor = instance(),
                accessTokenDao = instance()
            )
        }

        // Data Source
        bind<AuthNetworkDataSource>() with singleton {
            AuthNetworkDataSourceImpl(
                authApiService = instance()
            )
        }
        bind<WeatherNetworkDataSource>() with singleton {
            WeatherNetworkDataSourceImpl(
                apiService = instance()
            )
        }

        // Provider
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton {
            LocationProviderImpl(
                fusedLocationProviderClient = instance(),
                context = instance()
            )
        }
        bind<UnitProvider>() with singleton { UnitProviderImpl(context = instance()) }

        // Repository
        bind<AuthApiRepository>() with singleton {
            AuthApiRepositoryImpl(
                dao = instance(),
                dataSource = instance()
            )
        }
        bind<WeatherRepository>() with singleton {
            WeatherRepositoryImpl(
                currentWeatherDao = instance(),
                forecastDao = instance(),
                locationDao = instance(),
                dataSource = instance(),
                locationProvider = instance()
            )
        }

        // ViewModelFactory
        bind() from provider {
            CurrentWeatherViewModelFactory(
                weatherRepository = instance(),
                authApiRepository = instance(),
                unitProvider = instance()
            )
        }
        bind() from provider {
            ForecastWeatherViewModelFactory(
                repository = instance(),
                unitProvider = instance()
            )
        }
        bind() from provider {
            AuthViewModelFactory(
                repository = instance()
            )
        }
    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}