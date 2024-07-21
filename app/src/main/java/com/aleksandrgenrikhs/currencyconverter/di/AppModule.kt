package com.aleksandrgenrikhs.currencyconverter.di

import com.aleksandrgenrikhs.currencyconverter.data.ConvertRepositoryImpl
import com.aleksandrgenrikhs.currencyconverter.di.viewModel.ViewModelModule
import com.aleksandrgenrikhs.currencyconverter.domain.ConvertRepository
import com.aleksandrgenrikhs.currencyconverter.domain.NetworkConnectionChecker
import com.aleksandrgenrikhs.currencyconverter.utils.NetworkConnected
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        ViewModelModule::class,
        NetworkModule::class,
    ]
)
interface AppModule {

    @Binds
    @Singleton
    fun bindRepository(impl: ConvertRepositoryImpl): ConvertRepository

    companion object {
        @Provides
        @Singleton
        fun provideNetworkConnected(): NetworkConnectionChecker = NetworkConnected
    }
}