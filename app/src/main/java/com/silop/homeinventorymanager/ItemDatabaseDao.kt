package com.silop.homeinventorymanager

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDatabaseDao {

    @Insert
    fun insert(item: Item)

    @Update
    fun update(item: Item)

    @Delete
    fun delete(item: Item)

    @Query("SELECT * FROM item_table WHERE item_id = :key ORDER BY item_last_needed DESC")
    fun get(key: Long): Item?

    @Query("SELECT * FROM item_table WHERE item_name LIKE :name ORDER BY item_last_needed DESC")
    fun getName(name: String): Flow<List<Item>>

    @Query("SELECT * FROM item_table ORDER BY item_last_needed DESC")
    fun getAllItems(): Flow<List<Item>>
}