package ru.shamsutoff.choosingcitytestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import ru.shamsutoff.choosingcitytestapp.ui.navigation.AppNavGraph
import ru.shamsutoff.choosingcitytestapp.ui.navigation.SelectedCityHolder
import ru.shamsutoff.choosingcitytestapp.ui.theme.ChoosingCityTestAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChoosingCityTestAppTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    AppNavGraph(
                        navController = navController,
                        onCitySelected = { city ->
                            SelectedCityHolder.selectedCity = city
                        }
                    )
                }
            }
        }
    }
}
