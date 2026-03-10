package dev.vili.spot.data.repository

import dev.vili.spot.data.api.SpotHintaApi
import dev.vili.spot.data.model.SpotPrice
import dev.vili.spot.data.model.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class SpotPriceRepository(
    private val api: SpotHintaApi
) {

    suspend fun getCurrentPrice(lookForwardHours: Int = 0): Result<SpotPrice> =
        withContext(Dispatchers.IO) {
            safeApiCall {
                api.getJustNow(lookForwardHours = lookForwardHours).toDomain()
            }
        }

    suspend fun getTodayPrices(): Result<List<SpotPrice>> =
        withContext(Dispatchers.IO) {
            safeApiCall {
                api.getToday()
                    .toDomain()
                    .sortedBy { it.dateTime }
            }
        }

    private inline fun <T> safeApiCall(block: () -> T): Result<T> {
        return try {
            Result.success(block())
        } catch (e: IOException) {
            e.printStackTrace()
            Result.failure(
                Exception(
                    "Network error (${e::class.simpleName}): ${e.message ?: "Check your connection and try again."}",
                    e
                )
            )
        } catch (e: HttpException) {
            e.printStackTrace()
            Result.failure(
                Exception(
                    "Server error (${e.code()}): ${e.message ?: "Request failed."}",
                    e
                )
            )
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            Result.failure(
                Exception(
                    "Invalid API payload (${e::class.simpleName}): ${e.message ?: "Received invalid data from API."}",
                    e
                )
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(
                Exception(
                    "Unexpected error (${e::class.simpleName}): ${e.message ?: "Failed to load spot prices."}",
                    e
                )
            )
        }
    }
}
