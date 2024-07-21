package com.aleksandrgenrikhs.currencyconverter.presentation

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.NavInflater
import androidx.navigation.fragment.NavHostFragment
import com.aleksandrgenrikhs.currencyconverter.R
import com.aleksandrgenrikhs.currencyconverter.databinding.ActivityMainBinding
import com.aleksandrgenrikhs.currencyconverter.presentation.viewmodel.MainViewModel
import com.aleksandrgenrikhs.currencyconverter.viewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val navHostFragment: NavHostFragment by lazy {
        supportFragmentManager.findFragmentById(R.id.main_activity_nav_host) as NavHostFragment
    }

    private val navController: NavController by lazy { navHostFragment.navController }
    private val graphInflater: NavInflater by lazy { navController.navInflater }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startMainFragment()
    }

    private fun startMainFragment() {
        val navGraph = graphInflater.inflate(R.navigation.app_graph)
        navController.graph = navGraph
        navController.navigate(R.id.mainFragment)
    }
}