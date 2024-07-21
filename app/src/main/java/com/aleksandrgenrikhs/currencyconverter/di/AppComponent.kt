package com.aleksandrgenrikhs.currencyconverter.di

import android.app.Application
import com.aleksandrgenrikhs.currencyconverter.presentation.fragment.ConvertFragment
import com.aleksandrgenrikhs.currencyconverter.presentation.fragment.MainFragment
import com.aleksandrgenrikhs.currencyconverter.di.viewModel.ViewModelFactory
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        AppModule::class,
    ]
)

@Singleton
interface AppComponent {
    companion object {

        fun init(application: Application): AppComponent {
            return DaggerAppComponent.factory().create(application)
        }
    }

    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance application: Application,
        ): AppComponent
    }

    val viewModelFactory: ViewModelFactory

   fun inject(mainFragment: MainFragment)

    fun inject(convertFragment: ConvertFragment)
}