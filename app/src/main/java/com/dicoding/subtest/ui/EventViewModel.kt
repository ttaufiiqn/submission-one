package com.dicoding.subtest.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.subtest.data.response.EventResponse
import com.dicoding.subtest.data.response.ListEventsItem
import com.dicoding.subtest.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventViewModel : ViewModel() {

    private val _activeEvents = MutableLiveData<List<ListEventsItem>>()
    val activeEvents: LiveData<List<ListEventsItem>> get() = _activeEvents

    private val _inactiveEvents = MutableLiveData<List<ListEventsItem>>()
    val inactiveEvents: LiveData<List<ListEventsItem>> get() = _inactiveEvents

    fun fetchActiveEvents() {
        ApiConfig.getApiService().getActiveEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _activeEvents.value = response.body()?.listEvents
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
            }
        })
    }

    fun fetchInactiveEvents() {
        ApiConfig.getApiService().getInactiveEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    _inactiveEvents.value = response.body()?.listEvents
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
            }
        })
    }
}
