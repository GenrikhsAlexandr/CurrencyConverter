package com.aleksandrgenrikhs.currencyconverter.data

import kotlinx.serialization.Serializable

@Serializable
data class RatesDto(
    val currency_name: String,
    val rate: String,
    val rate_for_amount: Double
)