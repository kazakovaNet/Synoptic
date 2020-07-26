package ru.kazakovanet.synoptic.data.network.api.dto.weather


import com.google.gson.annotations.SerializedName

data class ConditionDTO(
    val code: Int,
    val temperature: Int,
    val text: String
)