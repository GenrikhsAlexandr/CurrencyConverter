package com.aleksandrgenrikhs.currencyconverter.presentation.uistate

import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesItem

data class MainUIState(
    val currencyNameFrom: String = "",
    val currencyCodeFrom: String = "",
    val currencyNameTo: String = "",
    val currencyCodeTo: String = "",
    val amount: Double = 0.0,
    val currencyItems: List<CurrenciesItem>? = null,
    val isLoading: Boolean = false,
)
