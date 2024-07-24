package com.aleksandrgenrikhs.currencyconverter.presentation.uistate

data class ConvertUIState(
    val amountForFrom: Double = 0.00,
    val currencyFromCode: String = "",
    val updatedDate: String = "",
    val currencyToCode: String = "",
    val amountForTo: Double = 0.00,
    val isLoading: Boolean = false,
    )