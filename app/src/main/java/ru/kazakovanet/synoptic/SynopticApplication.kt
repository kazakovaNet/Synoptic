package ru.kazakovanet.synoptic

import android.app.Application
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import ru.kazakovanet.synoptic.data.db.SynopticDatabase

/**
 * Created by NKazakova on 27.06.2020.
 */

class SynopticApplication : Application(), KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@SynopticApplication))

        bind() from singleton { SynopticDatabase(this@SynopticApplication) }
        bind() from singleton { instance<SynopticDatabase>().currentWeatherDao() }
    }
}