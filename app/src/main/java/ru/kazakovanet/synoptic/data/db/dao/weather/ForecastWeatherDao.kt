package ru.kazakovanet.synoptic.data.db.dao.weather

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ru.kazakovanet.synoptic.data.db.entity.ForecastWeatherEntity

/**
 * Created by NKazakova on 24.07.2020.
 */
@Dao
interface ForecastWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(list: List<ForecastWeatherEntity>)

    @Query("select * from forecast_weather where date >= :startDate")
    fun getWeatherForecast(startDate: Long): LiveData<List<ForecastWeatherEntity>>

    @Query("select * from forecast_weather where date = :date")
    fun getDetailWeatherByDay(date: Long): LiveData<ForecastWeatherEntity>

    @Query("select count(id) from forecast_weather where date >= :startDate")
    fun countFutureWeather(startDate: Long): Int

    @Query("delete from forecast_weather where date < :firstDateToKeep")
    fun deleteOldEntries(firstDateToKeep: Long)
}