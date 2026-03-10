package dev.vili.spot.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.vili.spot.ui.viewmodel.ThemeMode

@Composable
fun SettingsScreen(
    showTaxIncluded: Boolean,
    onShowTaxIncludedChanged: (Boolean) -> Unit,
    selectedThemeMode: ThemeMode,
    onThemeModeChanged: (ThemeMode) -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean = false
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineSmall
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Price display",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = if (showTaxIncluded) "With tax (ALV)" else "Without tax",
                    style = MaterialTheme.typography.bodyLarge
                )

                Switch(
                    checked = showTaxIncluded,
                    onCheckedChange = onShowTaxIncludedChanged
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Theme",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                ThemeModeOptionRow(
                    title = "Follow system",
                    selected = selectedThemeMode == ThemeMode.SYSTEM,
                    onClick = { onThemeModeChanged(ThemeMode.SYSTEM) }
                )
                ThemeModeOptionRow(
                    title = "Light",
                    selected = selectedThemeMode == ThemeMode.LIGHT,
                    onClick = { onThemeModeChanged(ThemeMode.LIGHT) }
                )
                ThemeModeOptionRow(
                    title = "Dark",
                    selected = selectedThemeMode == ThemeMode.DARK,
                    onClick = { onThemeModeChanged(ThemeMode.DARK) }
                )
            }

            Button(
                onClick = onRefreshClick,
                enabled = !isRefreshing
            ) {
                Text(if (isRefreshing) "Refreshing..." else "Refresh")
            }
        }

        Text(
            text = "Made by Vili",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 25.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Thanks to spot-hinta.fi for the API!",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 5.dp),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun ThemeModeOptionRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
