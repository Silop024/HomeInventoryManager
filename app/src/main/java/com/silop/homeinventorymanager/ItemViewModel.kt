package com.silop.homeinventorymanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silop.homeinventorymanager.restapi.ItemApi
import com.silop.homeinventorymanager.restapi.itemApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.await
import retrofit2.converter.gson.GsonConverterFactory

class ItemViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<Item>>(emptyList())

    val items: Flow<List<Item>>
        get() = _items

    fun loadItems() {
        viewModelScope.launch {
            val items = itemApi.getItems()
            _items.value = items
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch {
            itemApi.createItem(item)
            val items = itemApi.getItems()
            _items.value = items
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch {
            itemApi.updateItem(item.id, item)
            val items = itemApi.getItems()
            _items.value = items
        }
    }

    fun removeItem(item: Item) {
        viewModelScope.launch {
            itemApi.deleteItem(item.id)
            val items = itemApi.getItems()
            _items.value = items
        }
    }
}