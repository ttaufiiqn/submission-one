package com.dicoding.subtest.ui

import androidx.compose.ui.window.application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.subtest.data.remote.response.DetailEventResponse
import com.dicoding.subtest.data.remote.retrofit.ApiConfig
import com.dicoding.subtest.data.repository.FavoriteRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailEventViewModel : ViewModel() {
    private val _event = MutableLiveData<DetailEventResponse?>()
    val event: LiveData<DetailEventResponse?> = _event

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
                    } else {
                        _error.value = "Failed to load event details"
                    }
                }

                override fun onFailure(call: Call<DetailEventResponse>, t: Throwable) {
                    _loading.value = false
                    _error.value = "Failed to load event details"
                }
            })
    }
}
