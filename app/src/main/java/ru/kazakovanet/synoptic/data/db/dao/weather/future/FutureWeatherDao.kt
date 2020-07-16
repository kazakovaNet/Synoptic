package ru.kazakovanet.synoptic.data.db.dao.weather.future

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherEntry

/**
 * Created by NKazakova on 03.07.2020.
 */
@Dao
interface FutureWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(futureWeatherEntries: List<FutureWeatherEntry>)

    @Query("select * from future_weather where date >= :startDate")
    fun getWeatherForecast(startDate: Long): LiveData<List<FutureWeatherEntry>>

    @Query("select * from future_weather where date = :date")
    fun getDetailWeatherByDay(date: Long): LiveData<FutureWeatherEntry>

    @Query("select count(id) from future_weather where date >= :startDate")
    fun countFutureWeather(startDate: Long): Int

    @Query("delete from future_weather where date < :firstDateToKeep")
    fun deleteOldEntries(firstDateToKeep: Long)
}