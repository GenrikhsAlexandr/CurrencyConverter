package com.aleksandrgenrikhs.currencyconverter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrikhs.currencyconverter.R
import com.aleksandrgenrikhs.currencyconverter.domain.ConvertInteractor
import com.aleksandrgenrikhs.currencyconverter.domain.model.Currencies
import com.aleksandrgenrikhs.currencyconverter.presentation.mapper.CurrenciesItemMapper
import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesItem
import com.aleksandrgenrikhs.currencyconverter.presentation.uistate.MainUIState
import com.aleksandrgenrikhs.currencyconverter.utils.bufferedSharedFlow
import com.aleksandrgenrikhs.currencyconverter.utils.ManagerDialog
import com.aleksandrgenrikhs.currencyconverter.utils.NetworkResponse
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val interactor: ConvertInteractor,
    private val mapper: CurrenciesItemMapper,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainUIState> = MutableStateFlow(MainUIState())
    val uiState = _uiState.asStateFlow()

    private val _showDialogEmitter: MutableSharedFlow<ManagerDialog> = bufferedSharedFlow()
    val showDialogEmitter = _showDialogEmitter.asSharedFlow()

    init {
        loadCurrencies()
    }

    private fun processCurrencies(cards: List<Currencies>) {
        val currenciesDropdownItems = cards.map { mapper.map(it) }
        _uiState.update { uiState ->
            uiState.copy(
                currencyItems = currenciesDropdownItems
            )
        }
    }

    private fun loadCurrencies() {
        viewModelScope.launch {
            _uiState.update { uiState ->
                uiState.copy(
                    isLoading = true
                )
            }
            if (interactor.isNetWorkConnected()) {
                when (val response = interactor.getCurrencies()) {
                    is NetworkResponse.Success -> {
                        processCurrencies(response.data)
                        _uiState.update { uiState ->
                            uiState.copy(
                                isLoading = false
                            )
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

    fun onFromCurrencySelected(currencyItem: CurrenciesItem) {
        _uiState.update { uiState ->
            uiState.copy(
                currencyNameFrom = currencyItem.currencyName,
                currencyCodeFrom = currencyItem.currencyCode
            )
        }
    }

    fun onToCurrencySelected(currencyItem: CurrenciesItem) {
        _uiState.update { uiState ->
            uiState.copy(
                currencyNameTo = currencyItem.currencyName,
                currencyCodeTo = currencyItem.currencyCode
            )
        }
    }

    fun validateAmount(amount: String) {
        if (amount.isEmpty()) {
            _uiState.update { uiState ->
                uiState.copy(
                    amount = 0.0
                )
            }
        } else {
            _uiState.update { uiState ->
                uiState.copy(
                    amount = amount.toDouble()
                )
            }
        }
    }
}