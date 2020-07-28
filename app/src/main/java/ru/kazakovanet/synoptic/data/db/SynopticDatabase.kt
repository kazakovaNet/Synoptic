package ru.kazakovanet.synoptic.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kazakovanet.synoptic.data.db.dao.auth.AccessTokenDao
import ru.kazakovanet.synoptic.data.db.dao.weather.YahooCurrentWeatherDao
import ru.kazakovanet.synoptic.data.db.dao.weather.YahooForecastDao
import ru.kazakovanet.synoptic.data.db.dao.weather.YahooLocationDao
import ru.kazakovanet.synoptic.data.db.dao.weather.future.FutureWeatherDao
import ru.kazakovanet.synoptic.data.db.dao.weather.future.FutureWeatherLocationDao
import ru.kazakovanet.synoptic.data.db.entity.*

/**
 * Created by NKazakova on 26.06.2020.
 */

@Database(
    entities = [
        FutureWeatherEntry::class,
        FutureWeatherLocation::class,
        AccessTokenEntity::class,
        YahooWeatherEntity::class,
        YahooForecastEntity::class,
        YahooLocationEntity::class
    ],
    version = 1
)
abstract class SynopticDatabase : RoomDatabase() {
    abstract fun futureWeatherDao(): FutureWeatherDao
    abstract fun futureWeatherLocationDao(): FutureWeatherLocationDao
    abstract fun accessTokenDao(): AccessTokenDao
    abstract fun yahooCurrentWeatherDao(): YahooCurrentWeatherDao
    abstract fun yahooForecastDao(): YahooForecastDao
    abstract fun yahooLocationDao(): YahooLocationDao
}