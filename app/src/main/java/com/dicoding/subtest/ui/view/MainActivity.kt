package com.dicoding.subtest.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.dicoding.subtest.R
import com.dicoding.subtest.data.local.SettingPreferences
import com.dicoding.subtest.data.local.dataStore
import com.dicoding.subtest.data.local.repository.FavoriteEventRepository
import com.dicoding.subtest.data.local.room.FavoriteEventDatabase
import com.dicoding.subtest.ui.viewModel.FavoriteViewModel
import com.dicoding.subtest.ui.viewModel.FavoriteViewModelFactory
import com.dicoding.subtest.ui.viewModel.MainViewModel
import com.dicoding.subtest.ui.viewModel.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel =
            ViewModelProvider(this, ViewModelFactory(pref))[MainViewModel::class.java]

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)

        val database = FavoriteEventDatabase.getInstance(this)
        val repository = FavoriteEventRepository(database.favoriteEventDao())

        favoriteViewModel =
            FavoriteViewModelFactory(repository).create(FavoriteViewModel::class.java)
    }
}
