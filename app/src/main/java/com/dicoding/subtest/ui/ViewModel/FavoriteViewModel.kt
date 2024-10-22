package com.dicoding.subtest.ui.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.data.local.repository.FavoriteEventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: FavoriteEventRepository) : ViewModel() {
    fun getAllFavoriteEvents() = repository.getAllFavoriteEvents()
    fun getFavoriteEventById(id: String) = repository.getFavoriteEventById(id)
    fun insert(favoriteEvent: FavoriteEvent) = viewModelScope.launch {
        repository.insert(favoriteEvent)
    }
    fun delete(favoriteEvent: FavoriteEvent) = viewModelScope.launch {
        repository.delete(favoriteEvent)
    }
}
class FavoriteViewModelFactory(private val repository: FavoriteEventRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteViewModel(repository) as T
    }
}