package ru.shamsutoff.choosingcitytestapp.data.repository

import ru.shamsutoff.choosingcitytestapp.data.api.CityApiService
import ru.shamsutoff.choosingcitytestapp.domain.model.City
import ru.shamsutoff.choosingcitytestapp.domain.repository.CitiesPage
import ru.shamsutoff.choosingcitytestapp.domain.repository.CityRepository

class CityRepositoryImpl(
    private val apiService: CityApiService
) : CityRepository {

    override suspend fun getCities(query: String, page: Int, limit: Int): Result<CitiesPage> {
        return runCatching {
            val response = apiService.getCities(query, page, limit)
            CitiesPage(
                cities = response.items.map { dto ->
                    City(
                        id = dto.id,
                        name = dto.name,
                        country = dto.country,
                        lat = dto.lat,
                        lon = dto.lon,
                        pop = dto.pop
                    )
                },
                page = response.page,
                total = response.total
            )
        }
    }
}
