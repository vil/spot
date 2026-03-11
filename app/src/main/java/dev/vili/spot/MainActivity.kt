package dev.vili.spot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.vili.spot.data.api.SpotApiClient
import dev.vili.spot.data.repository.SpotPriceRepository
import dev.vili.spot.ui.screens.AboutScreen
import dev.vili.spot.ui.screens.CurrentlyScreen
import dev.vili.spot.ui.screens.HourlyScreen
import dev.vili.spot.ui.theme.PorssisahkoTheme
import dev.vili.spot.ui.viewmodel.SpotViewModel
import dev.vili.spot.ui.viewmodel.SpotViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

// version string
const val VERSION = "1.0.1"

private data class BottomNavItem(
    val title: String,
    val icon: ImageVector
)

private enum class SpotTab {
    CURRENTLY,
    WHOLE_DAY,
    ABOUT
}

@Composable
fun MainScreen() {
    val repository = remember { SpotPriceRepository(SpotApiClient.api) }
    val spotViewModel: SpotViewModel = viewModel(
        factory = SpotViewModelFactory(repository)
    )

    val uiState by spotViewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val tabs = remember {
        listOf(
            BottomNavItem("Currently", Icons.Filled.Home),
            BottomNavItem("Whole day", Icons.Filled.DateRange),
            BottomNavItem("About", Icons.Filled.Info)
        )
    }

    val pagerState = rememberPagerState(initialPage = SpotTab.CURRENTLY.ordinal, pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(uiState.errorMessage) {
        val message = uiState.errorMessage ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(message)
        spotViewModel.clearError()
    }

    PorssisahkoTheme(darkTheme = isSystemInDarkTheme()) {
        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            bottomBar = {
                NavigationBar(containerColor = MaterialTheme.colorScheme.surfaceVariant) {
                    tabs.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = pagerState.currentPage == index,
                            onClick = {
                                coroutineScope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            icon = { Icon(item.icon, contentDescription = item.title) },
                            label = { Text(item.title) }
                        )
                    }
                }
            }
        ) { innerPadding ->
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
            ) { page ->
                when (SpotTab.entries[page]) {
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

                    SpotTab.ABOUT -> {
                        AboutScreen(
                            viewModel = spotViewModel,
                            contentPadding = innerPadding,
                            modifier = Modifier
                        )
                    }
                }
            }

            // keep bottom nav in sync when user swipes pages
            LaunchedEffect(pagerState.currentPage) {
                // no-op here: bottom bar reads pagerState.currentPage directly for selection
            }
        }
    }
}
