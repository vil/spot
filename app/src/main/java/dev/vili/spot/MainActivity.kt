package dev.vili.spot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.vili.spot.ui.theme.PorssisahkoTheme
import androidx.compose.ui.graphics.vector.ImageVector

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PorssisahkoTheme {
                MainScreen()
            }
        }
    }
}

// A simple data class to hold the info for our bottom buttons
data class BottomNavItem(
    val title: String,
    val icon: ImageVector
)

@Composable
fun MainScreen() {
    var selectedItemIndex by remember { mutableIntStateOf(0) }

    val items = listOf(
        BottomNavItem("Hourly", Icons.Filled.Add),
        BottomNavItem("Tomorrow", Icons.Filled.DateRange),
        BottomNavItem("Settings", Icons.Filled.Settings)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                // This explicitly keeps the bottom bar background matching your theme surface
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            ) {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        selected = selectedItemIndex == index,
                        onClick = { selectedItemIndex = index }
                    )
                }
            }
        }
    ) { innerPadding ->
        // This Modifier passes the padding down so the screens don't get hidden behind the bottom bar
        val screenModifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        // The Traffic Cop: Switch the UI based on the selected tab
        when (selectedItemIndex) {
            0 -> HourlyScreen(modifier = screenModifier)
            1 -> TomorrowScreen(modifier = screenModifier)
            2 -> SettingsScreen(modifier = screenModifier)
        }
    }
}

@Composable
fun HourlyScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Hourly Prices",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun TomorrowScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Tomorrow's Prices",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}