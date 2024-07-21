package com.aleksandrgenrikhs.currencyconverter.domain

import com.aleksandrgenrikhs.currencyconverter.domain.model.Convert
import com.aleksandrgenrikhs.currencyconverter.domain.model.Currencies
import com.aleksandrgenrikhs.currencyconverter.utils.NetworkResponse

interface ConvertRepository {

    suspend fun getCurrencies(): NetworkResponse<List <Currencies>>

    suspend fun getConvert(from: String, to: String, amount: Double): NetworkResponse<Convert>

    fun isNetWorkConnected(): Boolean
}