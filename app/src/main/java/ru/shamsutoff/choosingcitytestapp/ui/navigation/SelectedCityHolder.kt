package ru.shamsutoff.choosingcitytestapp.ui.navigation

import ru.shamsutoff.choosingcitytestapp.domain.model.City

/**
 * Simple holder for passing the selected city between screens.
 * Kept simple since the API has no "get city by id" endpoint.
 */
object SelectedCityHolder {
    var selectedCity: City? = null
}
