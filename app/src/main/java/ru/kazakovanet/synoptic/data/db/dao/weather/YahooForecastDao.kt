package ru.kazakovanet.synoptic.data.db.dao.weather

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kazakovanet.synoptic.data.db.entity.YahooForecastEntity

/**
 * Created by NKazakova on 24.07.2020.
 */
@Dao
interface YahooForecastDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(list: List<YahooForecastEntity>)

    @Query("select * from yahoo_forecast where date >= :startDate")
    fun getWeatherForecast(startDate: Long): LiveData<List<YahooForecastEntity>>

    @Query("select * from yahoo_forecast where date = :date")
    fun getDetailWeatherByDay(date: Long): LiveData<YahooForecastEntity>

    @Query("select count(id) from yahoo_forecast where date >= :startDate")
    fun countFutureWeather(startDate: Long): Int

    @Query("delete from yahoo_forecast where date < :firstDateToKeep")
    fun deleteOldEntries(firstDateToKeep: Long)
}