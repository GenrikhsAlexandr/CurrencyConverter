package com.aleksandrgenrikhs.currencyconverter.presentation.mapper

import com.aleksandrgenrikhs.currencyconverter.domain.model.Currencies
import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesItem
import com.aleksandrgenrikhs.nivkhalphabet.utils.Mapper
import javax.inject.Inject

class CurrenciesItemMapper
@Inject constructor() : Mapper<Currencies, CurrenciesItem> {

    override fun map(input: Currencies): CurrenciesItem =
        CurrenciesItem(
            currencyName = input.currencyName,
            currencyCode = input.currencyCode
        )
}