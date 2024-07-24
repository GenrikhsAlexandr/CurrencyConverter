package com.aleksandrgenrikhs.currencyconverter.utils

import androidx.annotation.StringRes

sealed class ManagerDialog {
    data class Error(val message: String) : ManagerDialog()
    data class IsNotNetworkConnected(
        @StringRes val message: Int
    ) : ManagerDialog()
}