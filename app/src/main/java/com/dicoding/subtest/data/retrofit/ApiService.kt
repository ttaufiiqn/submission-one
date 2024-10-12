package com.dicoding.subtest.data.retrofit

import com.dicoding.subtest.data.response.DetailEventResponse
import com.dicoding.subtest.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events?active=1")
    fun getActiveEvents(): Call<EventResponse>

    @GET("events?active=0")
    fun getInactiveEvents(): Call<EventResponse>

    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: String): Call<DetailEventResponse>

}