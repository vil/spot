package dev.vili.spot.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.vili.spot.data.model.SpotPrice
import dev.vili.spot.data.repository.SpotPriceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ThemeMode {
    SYSTEM,
    LIGHT,
    DARK
}

data class SettingsState(
    val showTaxIncluded: Boolean = true,
    val themeMode: ThemeMode = ThemeMode.SYSTEM
)

data class SpotUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val currentPrice: SpotPrice? = null,
    val dayPrices: List<SpotPrice> = emptyList(),
    val settings: SettingsState = SettingsState(),
    val errorMessage: String? = null
)

class SpotViewModel(
    private val repository: SpotPriceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpotUiState(isLoading = true))
    val uiState: StateFlow<SpotUiState> = _uiState.asStateFlow()

    init {
        refreshAll(initialLoad = true)
    }

    fun refreshAll(initialLoad: Boolean = false) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = initialLoad,
                    isRefreshing = !initialLoad,
                    errorMessage = null
                )
            }

            val currentResult = repository.getCurrentPrice()
            val dayResult = repository.getTodayPrices()

            val firstError = listOf(currentResult, dayResult)
                .firstOrNull { it.isFailure }
                ?.exceptionOrNull()
                ?.message

            _uiState.update { previous ->
                previous.copy(
                    isLoading = false,
                    isRefreshing = false,
                    currentPrice = currentResult.getOrNull() ?: previous.currentPrice,
                    dayPrices = dayResult.getOrNull() ?: previous.dayPrices,
                    errorMessage = firstError
                )
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun setShowTaxIncluded(enabled: Boolean) {
        _uiState.update {
            it.copy(settings = it.settings.copy(showTaxIncluded = enabled))
        }
    }

    fun setThemeMode(mode: ThemeMode) {
        _uiState.update {
            it.copy(settings = it.settings.copy(themeMode = mode))
        }
    }
}
