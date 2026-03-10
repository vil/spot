package dev.vili.spot.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import dev.vili.spot.ui.viewmodel.SpotViewModel

@Composable
fun AboutScreen(
    viewModel: SpotViewModel,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showTaxIncluded = uiState.settings.showTaxIncluded

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = "About this app",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Secure and minimal Android app to check the current electricity price in Finland.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = {
                val email = "v@vili.dev"
                val subject = "Pörssisähkö feedback"
                val uri = "mailto:$email?subject=${Uri.encode(subject)}".toUri()
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                try {
                    context.startActivity(intent)
                } catch (_: ActivityNotFoundException) {
                    // No email app available — silently ignore for now
                }
            }) {
                Text("Send feedback")
            }

            Button(onClick = {
                val url = "https://vili.dev"
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                try {
                    context.startActivity(intent)
                } catch (_: ActivityNotFoundException) {
                    // No browser available
                }
            }) {
                Text("Developer's website")
            }

            Button(onClick = {
                val url = "https://github.com/vil/spot"
                val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                try {
                    context.startActivity(intent)
                } catch (_: ActivityNotFoundException) {
                    // No browser available
                }
            }) {
                Text("View source (GitHub)")
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(bottom = 24.dp)
        ) {
            Text(
                text = "Made by Vili",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Thanks to spot-hinta.fi for the API!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
