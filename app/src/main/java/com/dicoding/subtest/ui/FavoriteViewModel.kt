package com.dicoding.subtest.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.data.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavoriteRepository = FavoriteRepository(application)

    val favoriteEvents: LiveData<List<FavoriteEvent>> = repository.getAllFavorite()
}