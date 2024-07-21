package com.aleksandrgenrikhs.currencyconverter.presentation.fragment

import android.content.Context
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import com.aleksandrgenrikhs.currencyconverter.R
import com.aleksandrgenrikhs.currencyconverter.app
import com.aleksandrgenrikhs.currencyconverter.databinding.FragmentConvertBinding
import com.aleksandrgenrikhs.currencyconverter.presentation.viewmodel.ConvertViewModel
import com.aleksandrgenrikhs.currencyconverter.presentation.factory.ConvertViewModelAssistedFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class ConvertFragment : Fragment() {

    private var _binding: FragmentConvertBinding? = null
    private val binding: FragmentConvertBinding get() = _binding!!

    private val args by navArgs<ConvertFragmentArgs>()

    private val navController: NavController by lazy {
        (requireActivity().supportFragmentManager.findFragmentById(R.id.main_activity_nav_host) as NavHostFragment)
            .navController
    }

    @Inject
    lateinit var convertViewModel: ConvertViewModelAssistedFactory
    private val viewModel: ConvertViewModel by viewModels {
        convertViewModel.create(
            args.currencies
        )
    }

    override fun onAttach(context: Context) {
        (app.appComponent.inject(this))
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConvertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onClickBack()
        subscribe()
    }

 private fun subscribe(){
        lifecycleScope.launch {
            with(binding) {
                viewModel.uiState.collect { uiState ->
                    if (uiState.isError) {
                        Toast.makeText(requireContext(), uiState.error, Toast.LENGTH_SHORT).show()
                    }
                    if (!uiState.isNetworkConnected) {
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.is_not_internet),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    binding.progressBar.isVisible = uiState.isLoading
                    binding.mainLayout.isVisible = !uiState.isLoading
                    txtDate.text = uiState.updatedDate
                    txtAmountCurrenciesFrom.text = uiState.amountForFrom.toString()
                    txtAmountCurrenciesTo.text = uiState.amountForTo.toString()
                    txtFromCurrencyCode.text = uiState.currencyFromCode
                    txtToCurrencyCode.text = uiState.currencyToCode
                }
            }
        }
    }

    private fun onClickBack() {
        binding.backButton.setOnClickListener {
            navController.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}