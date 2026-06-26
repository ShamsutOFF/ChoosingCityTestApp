package ru.shamsutoff.choosingcitytestapp.data.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import ru.shamsutoff.choosingcitytestapp.data.dto.CitiesResponseDto

class CityApiService(private val httpClient: HttpClient) {

    companion object {
        private const val BASE_URL = "http://dev-dep.tools.urent.tech:8080"
    }

    suspend fun getCities(query: String, page: Int, limit: Int): CitiesResponseDto {
        return httpClient.get("$BASE_URL/api/cities") {
            parameter("query", query)
            parameter("page", page)
            parameter("limit", limit)
        }.body()
    }
}
