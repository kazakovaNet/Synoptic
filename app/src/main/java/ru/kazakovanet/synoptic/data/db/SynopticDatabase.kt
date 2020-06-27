package ru.kazakovanet.synoptic.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherEntry

/**
 * Created by NKazakova on 26.06.2020.
 */

@Database(entities = [CurrentWeatherEntry::class], version = 1)
abstract class SynopticDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao

    companion object {
        @Volatile
        private var instance: SynopticDatabase? = null
        private val LOCK = Any()
    }

    operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
        instance ?: buildDatabase(context).also { instance = it }
    }

    private fun buildDatabase(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            SynopticDatabase::class.java, "synoptic.db"
        )
            .build()
}