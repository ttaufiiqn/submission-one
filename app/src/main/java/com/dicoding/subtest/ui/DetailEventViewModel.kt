package com.dicoding.subtest.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.subtest.data.local.entity.FavoriteEvent
import com.dicoding.subtest.data.remote.response.DetailEventResponse
import com.dicoding.subtest.data.remote.retrofit.ApiConfig
import com.dicoding.subtest.data.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavoriteRepository = FavoriteRepository(application)

    private val _event = MutableLiveData<DetailEventResponse?>()
    val event: LiveData<DetailEventResponse?> = _event

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchEventDetails(id: String) {
        if (_event.value != null) {
            return
        }

        _loading.value = true
        ApiConfig.getApiService().getDetailEvent(id)
            .enqueue(object : Callback<DetailEventResponse> {
                override fun onResponse(
                    call: Call<DetailEventResponse>,
                    response: Response<DetailEventResponse>
                ) {
                    _loading.value = false
                    if (response.isSuccessful) {
                        _event.value = response.body()
                        _error.value = null
                        checkFavoriteStatus(id)
                    } else {
                        _error.value = "Failed to load event details"
                    }
                }

                override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                    _loading.value = false
                    _error.value = "Failed to load event details: ${t.message}"
                }
            })
    }

    private fun checkFavoriteStatus(id: String) {
        repository.getFavoriteById(id).observeForever { favoriteEvent ->
            _isFavorite.value = favoriteEvent != null
        }
    }

    fun toggleFavorite() {
        val currentEvent = event.value?.event ?: return
        if (_isFavorite.value == true) {
            repository.delete(FavoriteEvent(currentEvent.id.toString(), currentEvent.name, currentEvent.mediaCover))
        } else {
            repository.insert(FavoriteEvent(currentEvent.id.toString(), currentEvent.name, currentEvent.mediaCover))
        }
    }
}