package com.aleksandrgenrikhs.currencyconverter

import android.app.Application
import android.content.Context
import androidx.fragment.app.Fragment
import com.aleksandrgenrikhs.currencyconverter.di.AppComponent
import com.aleksandrgenrikhs.pokemon.di.ComponentProvider

class ConvertApp : Application(), ComponentProvider {
    val appComponent: AppComponent by lazy {
        AppComponent.init(this)
    }

    override fun componentProvider() = appComponent
}

val Context.app: ConvertApp get() = applicationContext as ConvertApp
val Fragment.app: ConvertApp get() = requireActivity().app

val Fragment.viewModelFactory get() = app.appComponent.viewModelFactory