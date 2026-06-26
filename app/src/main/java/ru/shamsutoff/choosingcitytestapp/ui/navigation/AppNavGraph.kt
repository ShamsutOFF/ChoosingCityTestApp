package ru.shamsutoff.choosingcitytestapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ru.shamsutoff.choosingcitytestapp.domain.model.City
import ru.shamsutoff.choosingcitytestapp.ui.cities.CitiesScreen
import ru.shamsutoff.choosingcitytestapp.ui.citydetail.CityDetailScreen

object NavRoutes {
    const val CITIES = "cities"
    const val CITY_DETAIL = "city_detail"
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    onCitySelected: (City) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.CITIES
    ) {
        composable(NavRoutes.CITIES) {
            CitiesScreen(
                onCityClick = { city ->
                    onCitySelected(city)
                    navController.navigate(NavRoutes.CITY_DETAIL)
                }
            )
        }
        composable(NavRoutes.CITY_DETAIL) {
            CityDetailScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}
