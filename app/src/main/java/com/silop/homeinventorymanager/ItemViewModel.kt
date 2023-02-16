package com.silop.homeinventorymanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.silop.homeinventorymanager.restapi.ItemService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.await

class ItemViewModel : ViewModel() {
    private val itemService = ItemService.getService()

    private val _items = MutableStateFlow<List<Item>>(emptyList())

    val items: Flow<List<Item>>
        get() = _items

    fun loadItems() {
        viewModelScope.launch {
            val items = itemService.getItems()
            _items.value = items.await()
        }
    }

    fun addItem(item: Item) {
        viewModelScope.launch {
            itemService.createItem(item).await()
            val items = itemService.getItems()
            _items.value = items.await()
        }
    }

    fun removeItem(item: Item) {
        viewModelScope.launch {
            itemService.deleteUser(item.id).await()
            val items = itemService.getItems()
            _items.value = items.await()
        }
    }
}