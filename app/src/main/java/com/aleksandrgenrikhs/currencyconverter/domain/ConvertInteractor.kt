package com.aleksandrgenrikhs.currencyconverter.domain

import javax.inject.Inject

class ConvertInteractor
@Inject constructor(
    private val repository: ConvertRepository
) {

    fun isNetWorkConnected(): Boolean = repository.isNetWorkConnected()

    suspend fun getCurrencies() = repository.getCurrencies()

    suspend fun covertCurrencies(from: String, to: String, amount: Double) =
        repository.getConvert(from, to, amount)
}