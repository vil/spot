package dev.vili.spot.data.api

import dev.vili.spot.data.model.SpotPriceDto
import retrofit2.http.GET
import retrofit2.http.Query

interface SpotHintaApi {

    @GET("JustNow")
    suspend fun getJustNow(
        @Query("lookForwardHours") lookForwardHours: Int = 0,
        @Query("isHtmlRequest") isHtmlRequest: Boolean = true
    ): SpotPriceDto

    @GET("Today")
    suspend fun getToday(
        @Query("isHtmlRequest") isHtmlRequest: Boolean = true
    ): List<SpotPriceDto>
}
