package ru.kazakovanet.synoptic.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kazakovanet.synoptic.data.db.dao.auth.AccessTokenDao
import ru.kazakovanet.synoptic.data.db.dao.weather.CurrentWeatherDao
import ru.kazakovanet.synoptic.data.db.dao.weather.ForecastWeatherDao
import ru.kazakovanet.synoptic.data.db.dao.weather.LocationDao
import ru.kazakovanet.synoptic.data.db.entity.AccessTokenEntity
import ru.kazakovanet.synoptic.data.db.entity.ForecastWeatherEntity
import ru.kazakovanet.synoptic.data.db.entity.LocationEntity
import ru.kazakovanet.synoptic.data.db.entity.WeatherEntity

/**
 * Created by NKazakova on 26.06.2020.
 */

@Database(
    entities = [
        AccessTokenEntity::class,
        WeatherEntity::class,
        ForecastWeatherEntity::class,
        LocationEntity::class
    ],
    version = 1
)
abstract class SynopticDatabase : RoomDatabase() {
    abstract fun accessTokenDao(): AccessTokenDao
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun forecastWeatherDao(): ForecastWeatherDao
    abstract fun locationDao(): LocationDao
}