package ru.shamsutoff.choosingcitytestapp.ui.cities

import android.content.Context
import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.shamsutoff.choosingcitytestapp.domain.model.City
import ru.shamsutoff.choosingcitytestapp.domain.repository.CityRepository
import ru.shamsutoff.choosingcitytestapp.util.NetworkUtil

data class CitiesState(
    val query: String = "",
    val cities: List<City> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isOffline: Boolean = false,
    val error: String? = null,
    val page: Int = 1,
    val hasMore: Boolean = false
)

sealed interface CitiesSideEffect {
    data class OpenCityDetail(val city: City) : CitiesSideEffect
}

class CitiesViewModel(
    private val repository: CityRepository
) : ViewModel(), ContainerHost<CitiesState, CitiesSideEffect> {

    override val container = container<CitiesState, CitiesSideEffect>(CitiesState())

    fun onQueryChanged(query: String) = intent {
        val newQuery = query.trim()
        if (state.query == newQuery) return@intent
        reduce { state.copy(query = newQuery, page = 1, cities = emptyList()) }
    }

    fun loadCities(context: Context) = intent {
        if (state.isLoading || state.isLoadingMore) return@intent

        reduce { state.copy(isLoading = true, error = null) }

        val isOnline = NetworkUtil.isNetworkAvailable(context)
        if (!isOnline) {
            reduce {
                state.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    isOffline = true,
                    error = "Нет подключения к интернету"
                )
            }
            return@intent
        }

        val currentQuery = state.query
        val currentPage = state.page

        repository.getCities(
            query = currentQuery,
            page = currentPage,
            limit = 20
        ).onSuccess { page ->
            if (state.query != currentQuery) return@intent
            reduce {
                state.copy(
                    cities = if (currentPage == 1) page.cities else state.cities + page.cities,
                    isLoading = false,
                    isLoadingMore = false,
                    isOffline = false,
                    error = null,
                    hasMore = page.cities.size == 20
                )
            }
        }.onFailure { throwable ->
            if (state.query != currentQuery) return@intent
            reduce {
                state.copy(
                    isLoading = false,
                    isLoadingMore = false,
                    error = throwable.message ?: "Неизвестная ошибка"
                )
            }
        }
    }

    fun loadNextPage(context: Context) = intent {
        if (state.isLoadingMore || !state.hasMore) return@intent
        reduce { state.copy(isLoadingMore = true, page = state.page + 1) }
        loadCities(context)
    }

    fun retry(context: Context) = intent {
        reduce { state.copy(isOffline = false, error = null) }
        loadCities(context)
    }

    fun onCityClicked(city: City) = intent {
        postSideEffect(CitiesSideEffect.OpenCityDetail(city))
    }
}
