package com.silop.homeinventorymanager

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeInventoryApplication : Application() {

    lateinit var retrofit: Retrofit
    override fun onCreate() {
        super.onCreate()
        retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}