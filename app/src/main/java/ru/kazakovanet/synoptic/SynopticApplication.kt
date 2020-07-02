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
import ru.kazakovanet.synoptic.data.network.*
import ru.kazakovanet.synoptic.data.provider.LocationProvider
import ru.kazakovanet.synoptic.data.provider.LocationProviderImpl
import ru.kazakovanet.synoptic.data.provider.UnitProvider
import ru.kazakovanet.synoptic.data.provider.UnitProviderImpl
import ru.kazakovanet.synoptic.data.repository.SynopticRepository
import ru.kazakovanet.synoptic.data.repository.SynopticRepositoryImpl
import ru.kazakovanet.synoptic.ui.weather.current.CurrentWeatherViewModelFactory

/**
 * Created by NKazakova on 27.06.2020.
 */

class SynopticApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@SynopticApplication))

        bind<SynopticDatabase>() with singleton {
            Room.databaseBuilder(
                this@SynopticApplication,
                SynopticDatabase::class.java,
                "synoptic.db"
            ).build()
        }
        bind() from singleton { instance<SynopticDatabase>().currentWeatherDao() }
        bind() from singleton { instance<SynopticDatabase>().weatherLocationDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherStackApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(), instance()) }
        bind<SynopticRepository>() with singleton {
            SynopticRepositoryImpl(
                currentWeatherDao = instance(),
                weatherLocationDao = instance(),
                weatherNetworkDataSource = instance(),
                locationProvider = instance()
            )
        }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
    }

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false)
    }
}