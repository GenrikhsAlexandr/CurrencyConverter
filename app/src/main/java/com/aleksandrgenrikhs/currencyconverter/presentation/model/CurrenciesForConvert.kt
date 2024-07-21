package com.aleksandrgenrikhs.currencyconverter.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CurrenciesForConvert(
    val currencyCodeFrom:String,
    val currencyCodeTo:String,
    val amount:Double,
): Parcelable
