package com.aleksandrgenrikhs.currencyconverter.di.viewModel

import androidx.lifecycle.ViewModel
import com.aleksandrgenrikhs.currencyconverter.presentation.viewmodel.MainViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @Singleton
    @ViewModelKey(MainViewModel::class)
    fun provideMainViewModel(mainViewModel: MainViewModel): ViewModel
}