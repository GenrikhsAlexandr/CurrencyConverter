package com.aleksandrgenrikhs.currencyconverter.data

import android.app.Application
import com.aleksandrgenrikhs.currencyconverter.data.mapper.ConvertMapper
import com.aleksandrgenrikhs.currencyconverter.data.mapper.CurrenciesMapper
import com.aleksandrgenrikhs.currencyconverter.domain.model.Convert
import com.aleksandrgenrikhs.currencyconverter.domain.ConvertRepository
import com.aleksandrgenrikhs.currencyconverter.domain.NetworkConnectionChecker
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
                    val body = response.body()
                    if (body != null) {
                        return@withContext NetworkResponse.Success(currenciesMapper.map(response.body()!!))
                    }
                }
                val errorBody = response.errorBody()
                if (errorBody != null) {
                    val errorObject =
                        Json.decodeFromString(ErrorObject.serializer(), errorBody.string())
                    return@withContext NetworkResponse.Error(errorObject)
                }
                return@withContext NetworkResponse.Error(
                    ErrorObject(
                        status = "unknown",
                        error = ErrorObject.Error(
                            message = response.message(),
                            code = response.code().toString()
                        )
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext NetworkResponse.Error(
                    ErrorObject(
                        status = "unknown",
                        error = ErrorObject.Error(
                            message = e.message ?: "Unknown error",
                            code = "-1"
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
                    val body = response.body()
                    if (body != null) {
                        return@withContext NetworkResponse.Success(convertMapper.map(response.body()!!))
                    }
                }
                val errorBody = response.errorBody()
                if (errorBody != null) {
                    val errorObject =
                        Json.decodeFromString(ErrorObject.serializer(), errorBody.string())
                    return@withContext NetworkResponse.Error(errorObject)
                }
                return@withContext NetworkResponse.Error(
                    ErrorObject(
                        status = "unknown",
                        error = ErrorObject.Error(
                            message = response.message(),
                            code = response.code().toString()
                        )
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext NetworkResponse.Error(
                    ErrorObject(
                        status = "unknown",
                        error = ErrorObject.Error(
                            message = e.message ?: "Unknown error",
                            code = "-1"
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