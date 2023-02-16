package com.silop.homeinventorymanager

data class Item(
    var id: Int = 0,
    var name: String,
    var location: String,
    var amount: Int,
    var lastNeeded: String
)