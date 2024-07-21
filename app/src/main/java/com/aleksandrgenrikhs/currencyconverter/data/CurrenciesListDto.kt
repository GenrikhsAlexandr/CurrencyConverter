package com.aleksandrgenrikhs.currencyconverter.data

import kotlinx.serialization.Serializable

@Serializable
data class CurrenciesListDto(
    val currencies: Map<String, String>,
    val status: String
)