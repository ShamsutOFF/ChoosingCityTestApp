package ru.shamsutoff.choosingcitytestapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CityDto(
    val id: Long,
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val pop: Long
)

@Serializable
data class CitiesResponseDto(
    val items: List<CityDto>,
    val limit: Int,
    val page: Int,
    val total: Int
)
