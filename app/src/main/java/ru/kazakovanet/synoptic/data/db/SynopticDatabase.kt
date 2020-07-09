package ru.kazakovanet.synoptic.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kazakovanet.synoptic.data.db.dao.current.CurrentWeatherDao
import ru.kazakovanet.synoptic.data.db.dao.current.CurrentWeatherLocationDao
import ru.kazakovanet.synoptic.data.db.dao.future.FutureWeatherDao
import ru.kazakovanet.synoptic.data.db.dao.future.FutureWeatherLocationDao
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherEntry
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherEntry
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherLocation
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherLocation

/**
 * Created by NKazakova on 26.06.2020.
 */

@Database(
    entities = [
        CurrentWeatherEntry::class,
        FutureWeatherEntry::class,
        CurrentWeatherLocation::class,
        FutureWeatherLocation::class
    ],
    version = 1
)
abstract class SynopticDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun futureWeatherDao(): FutureWeatherDao
    abstract fun weatherLocationDao(): CurrentWeatherLocationDao
    abstract fun futureWeatherLocationDao(): FutureWeatherLocationDao
}