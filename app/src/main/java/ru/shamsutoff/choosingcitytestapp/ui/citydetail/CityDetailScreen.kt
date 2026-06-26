package ru.shamsutoff.choosingcitytestapp.ui.citydetail

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.shamsutoff.choosingcitytestapp.ui.navigation.SelectedCityHolder
import java.net.URLEncoder
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityDetailScreen(
    onBack: () -> Unit,
    viewModel: CityDetailViewModel = koinInject()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        SelectedCityHolder.selectedCity?.let { viewModel.setCity(it) }
    }

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CityDetailSideEffect.OpenBrowser -> {
                val encoded = URLEncoder.encode(sideEffect.query, "UTF-8")
                val url = "https://www.google.com/search?q=$encoded"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(intent)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Информация о городе",
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val city = state.city
        if (city != null) {
            val numberFormat = DecimalFormat("#,###").apply {
                val symbols = DecimalFormatSymbols().apply {
                    groupingSeparator = ' '
                }
                decimalFormatSymbols = symbols
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 80.dp)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    CityDetailItem(
                        label = "Город",
                        value = city.name
                    )

                    CityDetailItem(
                        label = "Страна",
                        value = city.country
                    )

                    CityDetailItem(
                        label = "Население",
                        value = "${numberFormat.format(city.pop)} чел"
                    )
                }
                Button(
                    onClick = { viewModel.onSearchClick() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 16.dp)
                        .align(Alignment.BottomCenter),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(
                        text = "Поиск информации о городе",
                        fontWeight = FontWeight.W600,
                        fontSize = 18.sp,
                        lineHeight = 26.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CityDetailItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.W500,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
    }
}