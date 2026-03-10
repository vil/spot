package dev.vili.spot.data.model

import com.google.gson.annotations.SerializedName

data class SpotPriceDto(
    @SerializedName("Rank")
    val rank: Int,
    @SerializedName("DateTime")
    val dateTime: String,
    @SerializedName("PriceNoTax")
    val priceNoTax: Double,
    @SerializedName("PriceWithTax")
    val priceWithTax: Double
)

data class SpotPrice(
    val rank: Int,
    val dateTime: String,
    val priceNoTax: Double,
    val priceWithTax: Double
)

fun SpotPriceDto.toDomain(): SpotPrice = SpotPrice(
    rank = rank,
    dateTime = dateTime,
    priceNoTax = priceNoTax,
    priceWithTax = priceWithTax
)

fun List<SpotPriceDto>.toDomain(): List<SpotPrice> = map { it.toDomain() }
