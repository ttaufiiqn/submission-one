package com.dicoding.subtest.ui.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.subtest.data.remote.response.EventResponse
import com.dicoding.subtest.data.remote.response.ListEventsItem
import com.dicoding.subtest.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel : ViewModel() {

    private val _activeEvents = MutableLiveData<List<ListEventsItem>>()
    val activeEvents: LiveData<List<ListEventsItem>> get() = _activeEvents

    private val _inactiveEvents = MutableLiveData<List<ListEventsItem>>()
    val inactiveEvents: LiveData<List<ListEventsItem>> get() = _inactiveEvents

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchActiveEvents() {
        if (_activeEvents.value != null && _activeEvents.value!!.isNotEmpty()) return
        ApiConfig.getApiService().getActiveEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _activeEvents.value = response.body()?.listEvents
                } else {
                    _error.value = "Failed to load active events: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _error.value = "Error: ${t.message}"
            }
        })
    }

    fun fetchInactiveEvents() {
        ApiConfig.getApiService().getInactiveEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                if (response.isSuccessful) {
                    _inactiveEvents.value = response.body()?.listEvents
                } else {
                    _error.value = "Failed to load inactive events: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _error.value = "Error: ${t.message}"
            }
        })
    }
}