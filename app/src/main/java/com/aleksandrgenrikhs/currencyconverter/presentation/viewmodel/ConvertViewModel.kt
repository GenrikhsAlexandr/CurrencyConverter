package com.aleksandrgenrikhs.currencyconverter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrikhs.currencyconverter.domain.ConvertInteractor
import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesForConvert
import com.aleksandrgenrikhs.currencyconverter.presentation.uistate.ConvertUIState
import com.aleksandrgenrikhs.currencyconverter.utils.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConvertViewModel @Inject constructor(
    val currencies: CurrenciesForConvert,
    val interactor: ConvertInteractor,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ConvertUIState> = MutableStateFlow(
        ConvertUIState(
            isNetworkConnected = interactor.isNetWorkConnected()
        )
    )
    val uiState = _uiState.asStateFlow()

    init {
        convertCurrencies()
    }

     fun convertCurrencies() {
        viewModelScope.launch {
            _uiState.update { uiState ->
                uiState.copy(
                    isLoading = true
                )
            }
            when (
                val response = interactor.covertCurrencies(
                    currencies.currencyCodeFrom,
                    currencies.currencyCodeTo,
                    currencies.amount
                )) {
                is NetworkResponse.Success -> {
                    with(response.data) {
                        _uiState.update { uiState ->
                            uiState.copy(
                                updatedDate = updatedDate,
                                currencyFromCode = baseCurrencyCode,
                                amountForFrom = amount,
                                currencyToCode = currencyCode,
                                amountForTo = rateForAmount
                            )
                        }
                    }
                }

                is NetworkResponse.Error -> {
                    println("currencies = ${response.errorObject.error.message}")
                    _uiState.update { uiState ->
                        uiState.copy(
                            isError = true,
                            error = response.errorObject.error.message
                        )
                    }
                }
            }
            _uiState.update { uiState ->
                uiState.copy(
                    isLoading = false
                )
            }
        }
    }
}