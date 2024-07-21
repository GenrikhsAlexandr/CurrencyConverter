package com.aleksandrgenrikhs.currencyconverter.presentation.uistate

data class ConvertUIState(
    val error: String? = null,
    val isError:Boolean=false,
    val amountForFrom: Double = 0.00,
    val currencyFromCode: String = "",
    val updatedDate: String = "",
    val currencyToCode: String = "",
    val amountForTo: Double = 0.00,
    val isNetworkConnected: Boolean,
    val isLoading: Boolean = false,
    )
