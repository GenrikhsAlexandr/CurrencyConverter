package com.aleksandrgenrikhs.currencyconverter.domain

import android.content.Context

interface NetworkConnectionChecker {

    fun isNetworkConnected(context: Context): Boolean
}