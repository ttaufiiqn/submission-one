package com.dicoding.subtest.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.dicoding.subtest.BuildConfig

object ApiConfig {
    private const val BASE_URL = BuildConfig.BASE_URL

    fun getApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}