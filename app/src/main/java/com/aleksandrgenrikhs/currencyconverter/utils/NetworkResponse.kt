package com.aleksandrgenrikhs.currencyconverter.utils

import kotlinx.serialization.Serializable


sealed class NetworkResponse<out T> {
    data class Success<out T>(val data: T) : NetworkResponse<T>()

    @Serializable
    data class Error(
        val errorObject: ErrorObject
    ) : NetworkResponse<Nothing>()
}

@Serializable
data class ErrorObject(
    val status: String,
    val error: Error
) {
    @Serializable
    data class Error(
        val message: String,
        val code: Int
    )
}