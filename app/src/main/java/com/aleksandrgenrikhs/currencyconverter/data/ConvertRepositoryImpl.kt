package com.aleksandrgenrikhs.currencyconverter.data

import android.app.Application
import com.aleksandrgenrikhs.currencyconverter.R
import com.aleksandrgenrikhs.currencyconverter.data.mapper.ConvertMapper
import com.aleksandrgenrikhs.currencyconverter.data.mapper.CurrenciesMapper
import com.aleksandrgenrikhs.currencyconverter.domain.ConvertRepository
import com.aleksandrgenrikhs.currencyconverter.domain.NetworkConnectionChecker
import com.aleksandrgenrikhs.currencyconverter.domain.model.Convert
import com.aleksandrgenrikhs.currencyconverter.domain.model.Currencies
import com.aleksandrgenrikhs.currencyconverter.utils.ErrorObject
import com.aleksandrgenrikhs.currencyconverter.utils.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ConvertRepositoryImpl
@Inject constructor(
    private val application: Application,
    private val networkConnected: NetworkConnectionChecker,
    private val service: ApiService,
    private val currenciesMapper: CurrenciesMapper,
    private val convertMapper: ConvertMapper,
) : ConvertRepository {

    override suspend fun getCurrencies(): NetworkResponse<List<Currencies>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getCurrenciesList()
                if (response.isSuccessful) {
                    return@withContext NetworkResponse.Success(response.body()
                        ?.let { currenciesMapper.map(it) } ?: emptyList()
                    )
                } else {
                    val errorBody = response.errorBody()
                    val errorObject =
                        errorBody?.let {
                            Json.decodeFromString(
                                ErrorObject.serializer(),
                                it.string()
                            )
                        }
                    return@withContext errorObject?.let { NetworkResponse.Error(it) }
                        ?: NetworkResponse.Error(
                            ErrorObject(
                                status = application.getString(R.string.failed_status),
                                error = ErrorObject.Error(
                                    message = application.getString(R.string.unknown_error),
                                    code = -1
                                )
                            )
                        )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext NetworkResponse.Error(
                    ErrorObject(
                        status = application.getString(R.string.failed_status),
                        error = ErrorObject.Error(
                            message =e.message?: application.getString(R.string.unknown_error),
                            code = -1
                        )
                    )
                )
            }
        }
    }

    override suspend fun getConvert(
        from: String,
        to: String,
        amount: Double
    ): NetworkResponse<Convert> {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.convertCurrency(from = from, to = to, amount = amount)
                if (response.isSuccessful) {
                    response.body()?.let { convertMapper.map(it) }
                        ?.let { NetworkResponse.Success(it) }
                        ?: NetworkResponse.Error(
                            ErrorObject(
                                status = application.getString(R.string.failed_status),
                                error = ErrorObject.Error(
                                    message = application.getString(R.string.unknown_error),
                                    code = -1
                                )
                            )
                        )
                } else {
                    val errorBody = response.errorBody()
                    val errorObject =
                        errorBody?.let {
                            Json.decodeFromString(
                                ErrorObject.serializer(),
                                it.string()
                            )
                        }
                    errorObject?.let { NetworkResponse.Error(it) }
                        ?: NetworkResponse.Error(
                            ErrorObject(
                                status = application.getString(R.string.failed_status),
                                error = ErrorObject.Error(
                                    message = application.getString(R.string.unknown_error),
                                    code = -1
                                )
                            )
                        )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                NetworkResponse.Error(
                    ErrorObject(
                        status = application.getString(R.string.failed_status),
                        error = ErrorObject.Error(
                            message =e.message?: application.getString(R.string.unknown_error),
                            code = -1
                        )
                    )
                )
            }
        }
    }

    override fun isNetWorkConnected(): Boolean {
        return networkConnected.isNetworkConnected(application)
    }
}