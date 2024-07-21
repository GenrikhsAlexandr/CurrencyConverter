package com.aleksandrgenrikhs.currencyconverter.data.mapper

import com.aleksandrgenrikhs.currencyconverter.data.CurrenciesListDto
import com.aleksandrgenrikhs.currencyconverter.domain.model.Currencies
import com.aleksandrgenrikhs.nivkhalphabet.utils.Mapper
import javax.inject.Inject

class CurrenciesMapper
@Inject constructor() : Mapper<CurrenciesListDto, List<Currencies>> {

    override fun map(input: CurrenciesListDto): List<Currencies> =
        input.currencies.map { currency ->
            Currencies(
                currencyName = currency.value,
                currencyCode = currency.key
            )
        }.sortedBy { it.currencyName }
}