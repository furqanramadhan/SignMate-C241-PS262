package com.capstone.signmate_c241_ps262.data.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object{
        private lateinit var apiService: ApiService
    }
    fun getApiService(): ApiService{
        val retrofit = Retrofit.Builder()
            .baseUrl("signmate-7778d.firebaseapp.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        apiService = retrofit.create(ApiService::class.java)
        return apiService
    }
}