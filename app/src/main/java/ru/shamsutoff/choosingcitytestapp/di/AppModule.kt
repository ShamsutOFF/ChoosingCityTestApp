package ru.shamsutoff.choosingcitytestapp.di

import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import ru.shamsutoff.choosingcitytestapp.data.api.CityApiService
import ru.shamsutoff.choosingcitytestapp.data.repository.CityRepositoryImpl
import ru.shamsutoff.choosingcitytestapp.domain.repository.CityRepository
import ru.shamsutoff.choosingcitytestapp.ui.cities.CitiesViewModel
import ru.shamsutoff.choosingcitytestapp.ui.citydetail.CityDetailViewModel

val appModule = module {

    single {
        HttpClient(Android) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
            install(Logging) {
                level = LogLevel.BODY
            }
        }
    }

    single { CityApiService(get()) }

    single<CityRepository> { CityRepositoryImpl(get()) }

    factory { CitiesViewModel(get()) }

    factory { CityDetailViewModel() }
}
