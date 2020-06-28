package ru.kazakovanet.synoptic.data.repository

import androidx.lifecycle.LiveData
import ru.kazakovanet.synoptic.data.db.entity.CurrentWeatherEntry

/**
 * Created by NKazakova on 27.06.2020.
 */

interface SynopticRepository {
    suspend fun getCurrentWeather(): LiveData<out CurrentWeatherEntry>
}