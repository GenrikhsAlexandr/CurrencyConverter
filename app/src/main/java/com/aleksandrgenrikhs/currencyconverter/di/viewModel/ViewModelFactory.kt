package com.aleksandrgenrikhs.currencyconverter.di.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

class ViewModelFactory @Inject constructor(
    private val viewModelFactories: Map<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModelProvider = viewModelFactories[modelClass as Class<ViewModel>]
            ?: throw IllegalArgumentException("Declare $modelClass in ${ViewModelModule::class.simpleName}")
        return viewModelProvider.get() as T
    }
}