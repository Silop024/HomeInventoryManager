package com.silop.homeinventorymanager

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "item_table")
data class Item(
    @ColumnInfo(name = "item_id")
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo(name = "Item_name")
    var name: String,

    @ColumnInfo(name = "item_location")
    var location: String,

    @ColumnInfo(name = "item_amount")
    var amount: Int,

    @ColumnInfo(name = "item_last_needed")
    var lastNeeded: String)