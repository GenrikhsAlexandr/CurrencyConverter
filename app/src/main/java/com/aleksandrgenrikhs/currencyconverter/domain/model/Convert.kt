package com.aleksandrgenrikhs.currencyconverter.domain.model

data class Convert(
    val amount: Double,
    val baseCurrencyCode: String,
    val baseCurrencyName: String,
    val status: String,
    val updatedDate: String,
    val currencyName: String,
    val currencyCode:String,
    val rate: String,
    val rateForAmount: Double
)