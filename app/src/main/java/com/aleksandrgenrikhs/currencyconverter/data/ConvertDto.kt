package com.aleksandrgenrikhs.currencyconverter.data

import kotlinx.serialization.Serializable

@Serializable
data class ConvertDto(
    val amount: Double,
    val base_currency_code: String,
    val base_currency_name: String,
    val rates: Map<String, RatesDto>,
    val status: String,
    val updated_date: String
)