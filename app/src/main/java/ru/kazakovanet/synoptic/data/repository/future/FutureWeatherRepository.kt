package ru.kazakovanet.synoptic.data.repository.future

import androidx.lifecycle.LiveData
import org.threeten.bp.LocalDate
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherEntry
import ru.kazakovanet.synoptic.data.db.entity.FutureWeatherLocation

/**
 * Created by NKazakova on 06.07.2020.
 */

interface FutureWeatherRepository {
    suspend fun getFutureWeatherList(
        startDate: LocalDate,
        unitSystem: String
    ): LiveData<out List<FutureWeatherEntry>>

    suspend fun getFutureWeatherLocation(): LiveData<FutureWeatherLocation>
}