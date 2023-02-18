package com.silop.homeinventorymanager.restapi


import com.silop.homeinventorymanager.Item
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ItemApi {
    @GET("items")
    suspend fun getItems(): List<Item>
    @POST("items")
    suspend fun createItem(@Body item: Item): Item
    @PUT("items/{id}")
    suspend fun updateItem(@Path("id") id: Int, @Body item: Item): Boolean
    @DELETE("items/{id}")
    suspend fun deleteItem(@Path("id") id: Int)
}

private val retrofit = Retrofit.Builder()
    .baseUrl("http://192.168.0.10:8080")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val itemApi: ItemApi = retrofit.create(ItemApi::class.java)