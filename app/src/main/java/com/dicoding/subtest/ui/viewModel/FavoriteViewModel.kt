package com.dicoding.subtest.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.data.local.repository.FavoriteEventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteEventRepository) : ViewModel() {

    fun getAllFavoriteEvents() = repository.getAllFavoriteEvents()

    fun isFavorite(eventId: String): LiveData<FavoriteEvent?> =
        repository.getFavoriteEventById(eventId)

    fun insert(favoriteEvent: FavoriteEvent) = viewModelScope.launch {
        repository.insert(favoriteEvent)
    }

    fun delete(favoriteEvent: FavoriteEvent) = viewModelScope.launch {
        repository.delete(favoriteEvent)
    }
}

