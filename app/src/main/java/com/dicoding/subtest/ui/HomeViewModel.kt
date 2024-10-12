package com.dicoding.subtest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.subtest.data.remote.response.EventResponse
import com.dicoding.subtest.data.remote.response.ListEventsItem
import com.dicoding.subtest.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _activeEvents = MutableLiveData<List<ListEventsItem>>()
    val activeEvents: LiveData<List<ListEventsItem>> get() = _activeEvents

    private val _completedEvents = MutableLiveData<List<ListEventsItem>>()
    val completedEvents: LiveData<List<ListEventsItem>> get() = _completedEvents

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    fun fetchActiveEvents() {
        if (_activeEvents.value != null) return
        _loading.value = true
        ApiConfig.getApiService().getActiveEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    _activeEvents.value = response.body()?.listEvents?.take(5)
                } else {
                    _error.value = "Failed to load active events"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _loading.value = false
                _error.value = "Failed to load active events"
            }
        })
    }

    fun fetchCompletedEvents() {
        if (_completedEvents.value != null) return
        _loading.value = true
        ApiConfig.getApiService().getInactiveEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _loading.value = false
                if (response.isSuccessful) {
                    _completedEvents.value = response.body()?.listEvents?.take(5)
                } else {
                    _error.value = "Failed to load completed events"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _loading.value = false
                _error.value = "Failed to load completed events"
            }
        })
    }
}