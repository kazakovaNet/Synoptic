package ru.kazakovanet.synoptic.data.db.dao.weather

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kazakovanet.synoptic.data.db.entity.WEATHER_ID
import ru.kazakovanet.synoptic.data.db.entity.YahooWeatherEntity

/**
 * Created by NKazakova on 23.07.2020.
 */
@Dao
interface YahooCurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntity: YahooWeatherEntity)

    @Query("select * from yahoo_current_weather where id = $WEATHER_ID")
    fun getWeather(): LiveData<YahooWeatherEntity>
}