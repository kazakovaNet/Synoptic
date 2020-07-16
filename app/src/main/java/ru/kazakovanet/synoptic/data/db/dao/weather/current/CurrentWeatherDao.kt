package ru.kazakovanet.synoptic.data.db.dao.weather.current

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kazakovanet.synoptic.data.db.entity.CURRENT_WEATHER_ID
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherEntry

/**
 * Created by NKazakova on 26.06.2020.
 */

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherEntry: CurrentWeatherEntry)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeather(): LiveData<CurrentWeatherEntry>
}