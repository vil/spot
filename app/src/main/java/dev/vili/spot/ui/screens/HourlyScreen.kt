package dev.vili.spot.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.vili.spot.data.model.SpotPrice
import dev.vili.spot.ui.viewmodel.SpotViewModel
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.abs

@Composable
fun HourlyScreen(
    viewModel: SpotViewModel,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val prices = uiState.dayPrices
    val isLoading = uiState.isLoading
    val isRefreshing = uiState.isRefreshing
    val errorMessage = uiState.errorMessage
    val showTaxIncluded = uiState.settings.showTaxIncluded

    val listState = rememberLazyListState()
    val currentIndex = rememberCurrentQuarterIndex(prices)

    LaunchedEffect(currentIndex, prices.size) {
        if (prices.isNotEmpty() && currentIndex in prices.indices) {
            listState.scrollToItem(currentIndex)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Whole Day", style = MaterialTheme.typography.headlineSmall)
            Button(
                onClick = { viewModel.refreshAll(initialLoad = false) },
                enabled = !isLoading && !isRefreshing
            ) {
                Text(if (isRefreshing) "Refreshing..." else "Refresh")
            }
        }

        if (errorMessage != null && prices.isEmpty()) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.errorContainer,
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(12.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
            return
        }

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp, bottom = 20.dp, top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(prices, key = { _, p -> "${p.dateTime}-${p.rank}" }) { index, price ->
                val highlighted = index == currentIndex
                PriceRowCard(
                    price = price,
                    showTaxIncluded = showTaxIncluded,
                    highlighted = highlighted
                )
            }

            if (isLoading && prices.isEmpty()) {
                item {
                    Text(
                        text = "Loading prices...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 18.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (errorMessage != null && prices.isNotEmpty()) {
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.errorContainer,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = errorMessage,
                            modifier = Modifier.padding(10.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PriceRowCard(
    price: SpotPrice,
    showTaxIncluded: Boolean,
    highlighted: Boolean
) {
    val parsed = runCatching { OffsetDateTime.parse(price.dateTime) }.getOrNull()
    val timeText = parsed?.format(DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())) ?: price.dateTime

    val shownValue = if (showTaxIncluded) price.priceWithTax else price.priceNoTax
    val shownLabel = if (showTaxIncluded) "With tax" else "Without tax"

    val bg = if (highlighted) {
        MaterialTheme.colorScheme.primary.copy(alpha = 0.14f)
    } else {
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(bg)
                .padding(horizontal = 14.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(timeText, style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "Rank #${price.rank}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (highlighted) {
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = "Now",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "${formatPriceInCents(shownValue)} snt/kWh",
                    style = MaterialTheme.typography.titleSmall
                )
                Text(
                    text = shownLabel,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun rememberCurrentQuarterIndex(prices: List<SpotPrice>): Int {
    if (prices.isEmpty()) return -1

    val now = OffsetDateTime.now()
    val todayDate = now.toLocalDate()
    val nowMin = now.hour * 60 + now.minute

    var bestIndex = 0
    var bestDistance = Int.MAX_VALUE

    prices.forEachIndexed { index, p ->
        val dt = runCatching { OffsetDateTime.parse(p.dateTime) }.getOrNull() ?: return@forEachIndexed
        if (dt.toLocalDate() != todayDate) return@forEachIndexed

        val itemMin = dt.hour * 60 + dt.minute
        val distance = abs(itemMin - nowMin)
        if (distance < bestDistance) {
            bestDistance = distance
            bestIndex = index
        }
    }

    return bestIndex
}

private fun formatPriceInCents(valueInEuros: Double): String {
    val cents = valueInEuros * 100.0
    return String.format(Locale.getDefault(), "%.3f", cents)
}
