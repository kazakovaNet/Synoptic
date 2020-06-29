package ru.kazakovanet.synoptic

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import ru.kazakovanet.synoptic.data.db.CurrentWeatherDao
import ru.kazakovanet.synoptic.data.db.SynopticDatabase
import ru.kazakovanet.synoptic.data.db.SynopticDatabase_Impl
import ru.kazakovanet.synoptic.data.network.*
import ru.kazakovanet.synoptic.data.repository.SynopticRepository
import ru.kazakovanet.synoptic.data.repository.SynopticRepositoryImpl

/**
 * Created by NKazakova on 27.06.2020.
 */

class SynopticApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@SynopticApplication))

        bind<SynopticDatabase>() with singleton { SynopticDatabase_Impl() }
        bind<CurrentWeatherDao>() with singleton { instance<SynopticDatabase>().currentWeatherDao() }
        bind<ConnectivityInterceptor>() with singleton { ConnectivityInterceptorImpl(instance()) }
        bind() from singleton { WeatherStackApiService(instance()) }
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<SynopticRepository>() with singleton { SynopticRepositoryImpl(instance(), instance()) }
    }
}