package com.aleksandrgenrikhs.currencyconverter.presentation.uistate

import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesForConvert
import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesItem

data class MainUIState(
    val error: String? = null,
    val isError:Boolean=false,
    val currencyNameFrom: String = "",
    val currencyCodeFrom: String = "",
    val currencyNameTo: String = "",
    val currencyCodeTo: String = "",
    val amount: Double = 0.0,
    val isNetworkConnected: Boolean,
    val currencyItems: List<CurrenciesItem>? = null,
    val isLoading: Boolean = false,
)
