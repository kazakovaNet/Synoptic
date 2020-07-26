package ru.kazakovanet.synoptic.data.network.api.dto.weather

import com.google.gson.annotations.SerializedName

data class YahooWeatherResponseDTO(
    @SerializedName("current_observation")
    val currentObservation: CurrentObservationDTO,
    val forecasts: List<ForecastDTO>,
    val location: LocationDTO
)