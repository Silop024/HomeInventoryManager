package com.silop.homeinventorymanager.restapi


import com.silop.homeinventorymanager.Item
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ItemService {
    @GET("items")
    fun getItems(): Call<List<Item>>

    @POST("items")
    fun createItem(@Body item: Item): Call<Item>

    @POST("items/{id}")
    fun deleteUser(@Path("id") id: Int): Call<Unit>
}