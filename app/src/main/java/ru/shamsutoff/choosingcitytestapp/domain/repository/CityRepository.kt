package ru.shamsutoff.choosingcitytestapp.domain.repository

import ru.shamsutoff.choosingcitytestapp.domain.model.City

interface CityRepository {

    suspend fun getCities(query: String, page: Int, limit: Int): Result<CitiesPage>
}

data class CitiesPage(
    val cities: List<City>,
    val page: Int,
    val total: Int
)
