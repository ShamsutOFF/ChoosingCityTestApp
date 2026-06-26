package ru.shamsutoff.choosingcitytestapp.ui.cities

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.koin.compose.koinInject
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import ru.shamsutoff.choosingcitytestapp.domain.model.City

@Composable
fun CitiesScreen(
    onCityClick: (City) -> Unit,
    viewModel: CitiesViewModel = koinInject()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    val listState = rememberLazyListState()

    viewModel.collectSideEffect { sideEffect ->
        when (sideEffect) {
            is CitiesSideEffect.OpenCityDetail -> {
                onCityClick(sideEffect.city)
            }
        }
    }

    LaunchedEffect(state.query) {
        viewModel.loadCities(context)
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false
            lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 3
                && state.hasMore && !state.isLoadingMore
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore) {
            viewModel.loadNextPage(context)
        }
    }

    Scaffold { innerPadding ->
        // Если ошибка - показываем специальный экран без заголовка и поиска
        if (state.isOffline) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                // Контент по центру
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Красный круг с крестиком
                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .background(Color(0xFFFF305A), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Ошибка",
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Нет подключения к интернету",
                        fontWeight = FontWeight.W600,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        fontSize = 23.sp,
                        lineHeight = 26.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Проверьте соединение и попробуйте снова.\nБез интернета данные не загрузятся",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.W400,
                        fontSize = 16.sp,
                        lineHeight = 22.sp
                    )
                }

                Button(
                    onClick = { viewModel.retry(context) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Попробовать снова",
                        fontWeight = FontWeight.W600,
                        fontSize = 18.sp,
                        lineHeight = 26.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Список городов",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 19.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp,
                    modifier = Modifier
                        .padding(top = 16.dp, bottom = 12.dp)
                        .fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.query,
                    onValueChange = viewModel::onQueryChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = {
                        Text(
                            text = "Введите название города",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                        )
                    },
                    leadingIcon = null,
                    trailingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Поиск",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.Transparent,
                        focusedContainerColor = Color(0xFFF6F6F7),
                        unfocusedContainerColor = Color(0xFFF6F6F7),
                        focusedTextColor = MaterialTheme.colorScheme.onSurface,
                        unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                        focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.6f
                        ),
                        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.6f
                        ),
                        cursorColor = MaterialTheme.colorScheme.primary,
                        focusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.6f
                        ),
                        unfocusedTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(
                            alpha = 0.6f
                        )
                    ),
                    textStyle = MaterialTheme.typography.bodyLarge,
                )

                Spacer(modifier = Modifier.height(8.dp))

                when {
                    state.isLoading -> {
                        CitiesLoadingSkeleton()
                    }

                    state.error != null -> {
                        // Другие ошибки (не оффлайн)
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = state.error ?: "Что-то пошло не так",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.error
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(onClick = { viewModel.retry(context) }) {
                                    Text("Повторить")
                                }
                            }
                        }
                    }

                    state.cities.isEmpty() && state.query.isNotBlank() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Города не найдены",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    else -> {
                        CitiesList(
                            cities = state.cities,
                            isLoadingMore = state.isLoadingMore,
                            listState = listState,
                            onCityClick = { viewModel.onCityClicked(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CitiesLoadingSkeleton() {
    LazyColumn {
        items(10) {
            SkeletonItem()
        }
    }
}

@Composable
private fun SkeletonItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = Color.Gray.copy(alpha = 0.3f),
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 12.dp)
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(16.dp),
                shape = RoundedCornerShape(4.dp),
                color = Color.Gray.copy(alpha = 0.2f)
            ) {}
        }

        HorizontalDivider(Modifier, thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.15f))
    }
}

@Composable
private fun CitiesList(
    cities: List<City>,
    isLoadingMore: Boolean,
    listState: LazyListState,
    onCityClick: (City) -> Unit
) {
    LazyColumn(state = listState) {
        items(cities, key = { it.id }) { city ->
            CityCard(city = city, onClick = { onCityClick(city) })
        }
        if (isLoadingMore) {
            items(3) {
                SkeletonItem()
            }
        }
    }
}

@Composable
private fun CityCard(city: City, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Иконка геолокации
            Icon(
                Icons.Outlined.LocationOn,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 12.dp)
            )

            Text(
                text = "${city.name}, ${city.country}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        HorizontalDivider(Modifier, thickness = 0.5.dp, color = Color.Gray.copy(alpha = 0.15f))
    }
}