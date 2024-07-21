package com.aleksandrgenrikhs.currencyconverter.presentation.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aleksandrgenrikhs.currencyconverter.domain.ConvertInteractor
import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesForConvert
import com.aleksandrgenrikhs.currencyconverter.presentation.viewmodel.ConvertViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class ConvertViewModelFactory
@AssistedInject constructor(
    @Assisted private val currencies: CurrenciesForConvert,
    private val interactor: ConvertInteractor,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConvertViewModel(currencies, interactor) as T
    }
}

@AssistedFactory
interface  ConvertViewModelAssistedFactory {

    fun create(
        @Assisted currencies: CurrenciesForConvert,
    ): ConvertViewModelFactory
}