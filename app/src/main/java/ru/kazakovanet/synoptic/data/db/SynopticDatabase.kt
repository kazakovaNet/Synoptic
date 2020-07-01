package ru.kazakovanet.synoptic.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherEntry
import ru.kazakovanet.synoptic.data.db.entity.WeatherLocation

/**
 * Created by NKazakova on 26.06.2020.
 */

@Database(entities = [CurrentWeatherEntry::class, WeatherLocation::class], version = 1)
abstract class SynopticDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao
}