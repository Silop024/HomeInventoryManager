package com.silop.homeinventorymanager.restapi


import com.silop.homeinventorymanager.Item
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ItemService {
    @GET("/items")
    fun getItems(): Call<List<Item>>

    @POST("/items")
    fun createItem(@Body item: Item): Call<Item>

    @POST("/items/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Unit>

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://localhost:8080")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        fun getService(): ItemService = retrofit.create(ItemService::class.java)
    }
}