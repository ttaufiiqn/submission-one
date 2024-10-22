package com.dicoding.subtest.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.data.local.repository.FavoriteEventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteEventRepository) : ViewModel() {

    // Retrieve all favorite events
    fun getAllFavoriteEvents() = repository.getAllFavoriteEvents()

    // Check if an event is a favorite
    fun isFavorite(eventId: String): LiveData<FavoriteEvent?> = repository.getFavoriteEventById(eventId)

    // Insert a new favorite event
    fun insert(favoriteEvent: FavoriteEvent) = viewModelScope.launch {
        repository.insert(favoriteEvent)
    }

    // Delete a favorite event
    fun delete(favoriteEvent: FavoriteEvent) = viewModelScope.launch {
        repository.delete(favoriteEvent)
    }
}

// Factory class for creating FavoriteViewModel instances
class FavoriteViewModelFactory(private val repository: FavoriteEventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
