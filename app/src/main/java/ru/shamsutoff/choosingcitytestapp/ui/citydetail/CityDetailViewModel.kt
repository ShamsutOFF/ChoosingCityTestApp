package ru.shamsutoff.choosingcitytestapp.ui.citydetail

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container
import ru.shamsutoff.choosingcitytestapp.domain.model.City

data class CityDetailState(
    val city: City? = null,
    val isLoading: Boolean = false
)

sealed interface CityDetailSideEffect {
    data class OpenBrowser(val query: String) : CityDetailSideEffect
}

class CityDetailViewModel : ViewModel(), ContainerHost<CityDetailState, CityDetailSideEffect> {

    override val container = container<CityDetailState, CityDetailSideEffect>(CityDetailState())

    fun setCity(city: City) = intent {
        reduce { state.copy(city = city, isLoading = false) }
    }

    fun onSearchClick() = intent {
        val name = state.city?.name ?: return@intent
        postSideEffect(CityDetailSideEffect.OpenBrowser(name))
    }
}
