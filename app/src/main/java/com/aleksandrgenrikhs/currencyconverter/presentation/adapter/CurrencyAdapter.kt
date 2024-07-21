package com.aleksandrgenrikhs.currencyconverter.presentation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.aleksandrgenrikhs.currencyconverter.R
import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesItem

class CurrencyAdapter(
    context: Context,
    private val resource: Int,
    private var list: List<CurrenciesItem>
) :
    ArrayAdapter<CurrenciesItem>(context, resource, list) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        val item = list[position]
        (view as TextView).text = item.currencyName
        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getDropDownView(position, convertView, parent)
        val item = list[position]
        (view as TextView).text = item.currencyName
        return view
    }
}