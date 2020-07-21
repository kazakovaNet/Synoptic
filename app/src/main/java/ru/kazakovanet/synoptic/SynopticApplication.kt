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
import org.kodein.di.generic.*
import ru.kazakovanet.synoptic.data.db.SynopticDatabase
import ru.kazakovanet.synoptic.data.network.ConnectivityInterceptor
import ru.kazakovanet.synoptic.data.network.ConnectivityInterceptorImpl
import ru.kazakovanet.synoptic.data.network.api.openweathermap.OpenWeatherMapApiService
import ru.kazakovanet.synoptic.data.network.api.weatherstack.WeatherStackApiService
import ru.kazakovanet.synoptic.data.network.api.yahoo.YahooWeatherApiService
import ru.kazakovanet.synoptic.data.network.datasource.auth.YahooAuthNetworkDataSource
import ru.kazakovanet.synoptic.data.network.datasource.auth.YahooAuthNetworkDataSourceImpl
import ru.kazakovanet.synoptic.data.network.datasource.weather.current.CurrentWeatherNetworkDataSource
import ru.kazakovanet.synoptic.data.network.datasource.weather.current.CurrentWeatherNetworkDataSourceImpl
import ru.kazakovanet.synoptic.data.network.datasource.weather.future.FutureWeatherNetworkDataSource
import ru.kazakovanet.synoptic.data.network.datasource.weather.future.FutureWeatherNetworkDataSourceImpl
import ru.kazakovanet.synoptic.data.provider.LocationProvider
import ru.kazakovanet.synoptic.data.provider.LocationProviderImpl
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.provider.UnitProviderImpl
import ru.kazakovanet.synoptic.data.repository.auth.YahooAuthApiRepository
import ru.kazakovanet.synoptic.data.repository.auth.YahooAuthApiRepositoryImpl
import ru.kazakovanet.synoptic.data.repository.weather.current.CurrentWeatherRepository
import ru.kazakovanet.synoptic.data.repository.weather.current.CurrentWeatherRepositoryImpl
import ru.kazakovanet.synoptic.data.repository.weather.future.FutureWeatherRepository
import ru.kazakovanet.synoptic.data.repository.weather.future.FutureWeatherRepositoryImpl
import ru.kazakovanet.synoptic.ui.auth.YahooAPIAuthenticateViewModelFactory
import ru.kazakovanet.synoptic.ui.weather.current.CurrentWeatherViewModelFactory
import ru.kazakovanet.synoptic.ui.weather.future.detail.FutureDetailWeatherViewModelFactory
import ru.kazakovanet.synoptic.ui.weather.future.list.FutureListWeatherViewModelFactory

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
        bind() from singleton { instance<SynopticDatabase>().currentWeatherDao() }
        bind() from singleton { instance<SynopticDatabase>().futureWeatherDao() }
        bind() from singleton { instance<SynopticDatabase>().weatherLocationDao() }
        bind() from singleton { instance<SynopticDatabase>().futureWeatherLocationDao() }
        bind() from singleton { instance<SynopticDatabase>().accessTokenDao() }

        // Interceptor
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(context = instance()) }

        // ApiService
        bind() from singleton { WeatherStackApiService(connectivityInterceptor = instance()) }
        bind() from singleton { OpenWeatherMapApiService(connectivityInterceptor = instance()) }
        bind() from singleton { YahooWeatherApiService(connectivityInterceptor = instance()) }

        // Data Source
        bind<CurrentWeatherNetworkDataSource>() with singleton {
            CurrentWeatherNetworkDataSourceImpl(
                weatherStackApiService = instance()
            )
        }
        bind<FutureWeatherNetworkDataSource>() with singleton {
            FutureWeatherNetworkDataSourceImpl(
                openWeatherMapApiService = instance()
            )
        }
        bind<YahooAuthNetworkDataSource>() with singleton {
            YahooAuthNetworkDataSourceImpl(
                yahooWeatherApiService = instance()
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
        bind<CurrentWeatherRepository>() with singleton {
            CurrentWeatherRepositoryImpl(
                currentWeatherDao = instance(),
                currentWeatherLocationDao = instance(),
                dataSource = instance(),
                locationProvider = instance()
            )
        }
        bind<FutureWeatherRepository>() with singleton {
            FutureWeatherRepositoryImpl(
                futureWeatherDao = instance(),
                futureWeatherLocationDao = instance(),
                dataSource = instance(),
                locationProvider = instance()
            )
        }
        bind<YahooAuthApiRepository>() with singleton {
            YahooAuthApiRepositoryImpl(
                dao = instance(),
                dataSource = instance()
            )
        }

        // ViewModelFactory
        bind() from provider {
            CurrentWeatherViewModelFactory(
                currentWeatherRepository = instance(),
                authApiRepository = instance(),
                unitProvider = instance()
            )
        }
        bind() from provider {
            FutureListWeatherViewModelFactory(
                repository = instance(),
                unitProvider = instance()
            )
        }
        bind() from factory { detailDate: Long ->
            FutureDetailWeatherViewModelFactory(
                detailDate = detailDate,
                repository = instance(),
                unitProvider = instance()
            )
        }
        bind() from provider {
            YahooAPIAuthenticateViewModelFactory(
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