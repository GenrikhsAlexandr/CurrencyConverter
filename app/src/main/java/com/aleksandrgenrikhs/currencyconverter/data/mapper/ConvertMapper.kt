package com.aleksandrgenrikhs.currencyconverter.data.mapper

import com.aleksandrgenrikhs.currencyconverter.data.ConvertDto
import com.aleksandrgenrikhs.currencyconverter.domain.model.Convert
import com.aleksandrgenrikhs.nivkhalphabet.utils.Mapper
import javax.inject.Inject

class ConvertMapper
@Inject constructor() : Mapper<ConvertDto, Convert> {

    override fun map(input: ConvertDto): Convert {
        with(input) {
            return Convert(
                amount = amount,
                baseCurrencyCode = base_currency_code,
                baseCurrencyName = base_currency_name,
                status = status,
                updatedDate = updated_date,
                currencyCode = rates.keys.first(),
                currencyName = rates.values.first().currency_name,
                rate = rates.values.first().rate,
                rateForAmount = rates.values.first().rate_for_amount
            )
        }
    }
}