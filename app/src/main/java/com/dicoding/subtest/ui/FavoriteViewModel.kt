package com.dicoding.subtest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.data.repository.FavoriteRepository

class FavoriteViewModel(private val repository: FavoriteRepository) : ViewModel() {
    val favoriteEvents: LiveData<List<FavoriteEvent>> = repository.getAllFavorite()

    fun insert(favoriteEvent: FavoriteEvent) {
        repository.insert(favoriteEvent)
    }

    fun delete(favoriteEvent: FavoriteEvent) {
        repository.delete(favoriteEvent)
    }

    fun getFavoriteById(id: String): LiveData<FavoriteEvent?> = repository.getFavoriteById(id)
}