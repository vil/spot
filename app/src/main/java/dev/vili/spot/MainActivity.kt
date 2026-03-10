package dev.vili.spot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.vili.spot.data.api.SpotApiClient
import dev.vili.spot.data.repository.SpotPriceRepository
import dev.vili.spot.ui.screens.CurrentlyScreen
import dev.vili.spot.ui.screens.HourlyScreen
import dev.vili.spot.ui.screens.SettingsScreen
import dev.vili.spot.ui.theme.PorssisahkoTheme
import dev.vili.spot.ui.viewmodel.SpotViewModel
import dev.vili.spot.ui.viewmodel.SpotViewModelFactory
import dev.vili.spot.ui.viewmodel.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

private data class BottomNavItem(
    val title: String,
    val icon: ImageVector
)

private enum class SpotTab {
    CURRENTLY,
    WHOLE_DAY,
    SETTINGS
}

@Composable
fun MainScreen() {
    val repository = remember { SpotPriceRepository(SpotApiClient.api) }
    val spotViewModel: SpotViewModel = viewModel(
        factory = SpotViewModelFactory(repository)
    )

    val uiState by spotViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val darkTheme = when (uiState.settings.themeMode) {
        ThemeMode.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
    }

    var selectedTab by remember { mutableIntStateOf(SpotTab.CURRENTLY.ordinal) }

    val tabs = remember {
        listOf(
            BottomNavItem("Currently", Icons.Filled.Home),
            BottomNavItem("Whole day", Icons.Filled.DateRange),
            BottomNavItem("Settings", Icons.Filled.Settings)
        )
    }

    LaunchedEffect(uiState.errorMessage) {
        val message = uiState.errorMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        spotViewModel.clearError()
    }

    PorssisahkoTheme(darkTheme = darkTheme) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                    tabs.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            when (SpotTab.entries[selectedTab]) {
                SpotTab.CURRENTLY -> {
                    CurrentlyScreen(
                        viewModel = spotViewModel,
                        contentPadding = innerPadding,
                        modifier = Modifier
                    )
                }

                SpotTab.WHOLE_DAY -> {
                    HourlyScreen(
                        viewModel = spotViewModel,
                        contentPadding = innerPadding,
                        modifier = Modifier
                    )
                }

                SpotTab.SETTINGS -> {
                    SettingsScreen(
                        showTaxIncluded = uiState.settings.showTaxIncluded,
                        onShowTaxIncludedChanged = spotViewModel::setShowTaxIncluded,
                        selectedThemeMode = uiState.settings.themeMode,
                        onThemeModeChanged = spotViewModel::setThemeMode,
                        onRefreshClick = { spotViewModel.refreshAll(initialLoad = false) },
                        isRefreshing = uiState.isRefreshing,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
