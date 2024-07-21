package com.aleksandrgenrikhs.currencyconverter.data

import com.aleksandrgenrikhs.currencyconverter.utils.UrlConstants.CONVERT_URL
import com.aleksandrgenrikhs.currencyconverter.utils.UrlConstants.CURRENCIES_LIST_URL
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET(CURRENCIES_LIST_URL)
    suspend fun getCurrenciesList(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "1b9e777049ee156cc8352a018c6564a9ebb5f2de",
    ): Response<CurrenciesListDto>

    @GET(CONVERT_URL)
    suspend fun convertCurrency(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "1b9e777049ee156cc8352a018c6564a9ebb5f2de",
        @Query(QUERY_FROM) from: String,
        @Query(QUERY_TO) to: String,
        @Query(QUERY_AMOUNT) amount: Double
    ): Response<ConvertDto>

    companion object {
        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val QUERY_FROM = "from"
        private const val QUERY_TO = "to"
        private const val QUERY_AMOUNT = "amount"
    }
}