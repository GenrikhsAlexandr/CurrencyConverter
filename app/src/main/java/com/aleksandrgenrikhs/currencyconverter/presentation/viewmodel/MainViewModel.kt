package com.aleksandrgenrikhs.currencyconverter.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aleksandrgenrikhs.currencyconverter.domain.ConvertInteractor
import com.aleksandrgenrikhs.currencyconverter.domain.model.Currencies
import com.aleksandrgenrikhs.currencyconverter.presentation.mapper.CurrenciesItemMapper
import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesItem
import com.aleksandrgenrikhs.currencyconverter.presentation.uistate.MainUIState
import com.aleksandrgenrikhs.currencyconverter.utils.NetworkResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val interactor: ConvertInteractor,
    private val mapper: CurrenciesItemMapper,
) : ViewModel() {

    private val _uiState: MutableStateFlow<MainUIState> = MutableStateFlow(
        MainUIState(
            isNetworkConnected = interactor.isNetWorkConnected(),
        )
    )
    val uiState = _uiState.asStateFlow()

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
            interactor.getCurrencies()
            when (val response = interactor.getCurrencies()) {
                is NetworkResponse.Success -> {
                    processCurrencies(response.data)
                }

                is NetworkResponse.Error -> {
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