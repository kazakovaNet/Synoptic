package ru.kazakovanet.synoptic.data.network.api.dto.weather


data class CurrentObservationDTO(
    val astronomy: AstronomyDTO,
    val atmosphere: AtmosphereDTO,
    val condition: ConditionDTO,
    val pubDate: Long,
    val wind: WindDTO
)