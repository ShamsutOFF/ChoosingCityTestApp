package ru.shamsutoff.choosingcitytestapp.domain.model

data class City(
    val id: Long,
    val name: String,
    val country: String,
    val lat: Double,
    val lon: Double,
    val pop: Long
)
