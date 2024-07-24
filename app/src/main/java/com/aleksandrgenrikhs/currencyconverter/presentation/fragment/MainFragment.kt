package com.aleksandrgenrikhs.currencyconverter.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.aleksandrgenrikhs.currencyconverter.R
import com.aleksandrgenrikhs.currencyconverter.databinding.FragmentMainBinding
import com.aleksandrgenrikhs.currencyconverter.presentation.adapter.CurrencyAdapter
import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesForConvert
import com.aleksandrgenrikhs.currencyconverter.presentation.model.CurrenciesItem
import com.aleksandrgenrikhs.currencyconverter.presentation.viewmodel.MainViewModel
import com.aleksandrgenrikhs.currencyconverter.utils.ManagerDialog
import com.aleksandrgenrikhs.currencyconverter.viewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding: FragmentMainBinding get() = _binding!!

    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    private var currenciesForConvert: CurrenciesForConvert? = null

    private val navController: NavController by lazy {
        (requireActivity().supportFragmentManager.findFragmentById(R.id.main_activity_nav_host) as NavHostFragment)
            .navController
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAmount()
        subscribe()
        binding.convertButton.setOnClickListener {
            currenciesForConvert?.let { convertButtonClicked(it) }
        }
    }

    private fun subscribe() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { uiState ->
                    binding.progressBar.isVisible = uiState.isLoading
                    binding.mainLayout.isVisible = !uiState.isLoading
                    uiState.currencyItems?.let {
                        initFromCurrencies(it) { selectedCurrency ->
                            viewModel.onFromCurrencySelected(selectedCurrency)
                        }
                        initToCurrencies(it) { selectedCurrency ->
                            viewModel.onToCurrencySelected(selectedCurrency)
                        }
                    }
                    binding.txtFromCurrencyCode.text = uiState.currencyCodeFrom
                    binding.txtSpnFromCountry.setText(uiState.currencyNameFrom, false)
                    binding.txtToCurrencyCode.text = uiState.currencyCodeTo
                    binding.txtSpnToCountry.setText(uiState.currencyNameTo, false)
                    binding.convertButton.isVisible = uiState.currencyNameFrom.isNotEmpty()
                            && uiState.currencyNameTo.isNotEmpty()
                            && uiState.amount != 0.0
                    currenciesForConvert = CurrenciesForConvert(
                        uiState.currencyCodeFrom,
                        uiState.currencyCodeTo,
                        uiState.amount
                    )
                }
            }
        }
       viewLifecycleOwner.lifecycleScope.launch {
            viewModel.showDialogEmitter.collect { showDialog ->
                when (showDialog) {
                    is ManagerDialog.Error -> Snackbar.make(
                        binding.root,
                        showDialog.message,
                        Snackbar.LENGTH_LONG
                    )
                        .setBackgroundTint(requireContext().getColor(R.color.colorOnPrimary))
                        .show()

                    is ManagerDialog.IsNotNetworkConnected ->  Snackbar.make(
                        binding.root,
                       showDialog.message,
                        Snackbar.LENGTH_LONG
                    )
                        .setBackgroundTint(requireContext().getColor(R.color.colorOnPrimary))
                        .show()
                }
            }
        }
    }

    private fun initFromCurrencies(
        list: List<CurrenciesItem>,
        selectedCardAction: (CurrenciesItem) -> Unit
    ) {
        val adapter = CurrencyAdapter(requireContext(), R.layout.list_currencies_item, list)
        val autoCompleteTextView = binding.spnFromCountry.editText as? AutoCompleteTextView
        autoCompleteTextView?.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                val selectedCurrency = list[position]
                setText(selectedCurrency.currencyName, false)
                selectedCardAction.invoke(selectedCurrency)
            }
        }
    }

    private fun initToCurrencies(
        list: List<CurrenciesItem>,
        selectedCurrencyAction: (CurrenciesItem) -> Unit
    ) {
        val adapter = CurrencyAdapter(requireContext(), R.layout.list_currencies_item, list)
        val autoCompleteTextView = binding.spnToCountry.editText as? AutoCompleteTextView
        autoCompleteTextView?.apply {
            setAdapter(adapter)
            setOnItemClickListener { _, _, position, _ ->
                val selectedCurrency = list[position]
                setText(selectedCurrency.currencyName, false)
                selectedCurrencyAction.invoke(selectedCurrency)
            }
        }
    }

    private fun initAmount() {
        binding.amountEditText.doAfterTextChanged { amount ->
            viewModel.validateAmount(amount.toString())
        }
    }

    private fun convertButtonClicked(argument: CurrenciesForConvert) {
        val action = MainFragmentDirections
            .actionMainFragmentToConvertFragment(
                argument
            )
        navController.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}