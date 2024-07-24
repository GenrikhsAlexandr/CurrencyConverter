package com.aleksandrgenrikhs.currencyconverter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrikhs.currencyconverter.R
import com.aleksandrgenrikhs.currencyconverter.domain.ConvertInteractor
import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesForConvert
import com.aleksandrgenrikhs.currencyconverter.presentation.uistate.ConvertUIState
import com.aleksandrgenrikhs.currencyconverter.utils.ManagerDialog
import com.aleksandrgenrikhs.currencyconverter.utils.NetworkResponse
import com.aleksandrgenrikhs.currencyconverter.utils.bufferedSharedFlow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConvertViewModel @Inject constructor(
    val currencies: CurrenciesForConvert,
    val interactor: ConvertInteractor,
) : ViewModel() {

    private val _uiState: MutableStateFlow<ConvertUIState> = MutableStateFlow(ConvertUIState())
    val uiState = _uiState.asStateFlow()

    private val _showDialogEmitter: MutableSharedFlow<ManagerDialog> = bufferedSharedFlow()
    val showDialogEmitter = _showDialogEmitter.asSharedFlow()

    init {
        convertCurrencies()
    }

    private fun convertCurrencies() {
        viewModelScope.launch {
            _uiState.update { uiState ->
                uiState.copy(
                    isLoading = true
                )
            }
            if (interactor.isNetWorkConnected()) {
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
                                    amountForTo = rateForAmount,
                                    isLoading = false

                                )
                            }
                        }
                    }

                    is NetworkResponse.Error -> {
                        _showDialogEmitter.tryEmit(ManagerDialog.Error(response.errorObject.error.message))
                    }
                }
            } else {
                _showDialogEmitter.tryEmit(ManagerDialog.IsNotNetworkConnected(R.string.is_not_internet))
            }
        }
    }
}