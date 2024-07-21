package com.aleksandrgenrikhs.pokemon.di

import com.aleksandrgenrikhs.currencyconverter.di.AppComponent

interface ComponentProvider {

    fun componentProvider(): AppComponent
}