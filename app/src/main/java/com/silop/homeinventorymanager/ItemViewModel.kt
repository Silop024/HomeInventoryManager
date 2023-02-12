package com.silop.homeinventorymanager

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silop.homeinventorymanager.restapi.ItemService
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.await

class ItemViewModel(private val application: Application) : ViewModel() {
    private val itemService = (application as HomeInventoryApplication).retrofit
        .create(ItemService::class.java)

    private val _items = MutableLiveData<List<Item>>()

    val items: LiveData<List<Item>> = _items

    fun loadItems() {
        viewModelScope.launch {
            val items = itemService.getItems()
            _items.value = items.await()
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch {
            itemService.createItem(item)
            val items = itemService.getItems()
            _items.value = items.await()
        }
    }

    fun removeItem(item: Item) {
        viewModelScope.launch {
            itemService.deleteUser(item.id)
            val items = itemService.getItems()
            _items.value = items.await()
        }
    }

    fun fetchItems() {
        itemService.getItems().enqueue(object : Callback<List<Item>> {
            override fun onResponse(call: Call<List<Item>>, response: Response<List<Item>>) {
                if (response.isSuccessful) {
                    _items.value = response.body()
                } else {
                    // TODO("Handle error")
                }
            }

            override fun onFailure(call: Call<List<Item>>, t: Throwable) {
                // TODO("Handle error")
            }
        })
    }
}