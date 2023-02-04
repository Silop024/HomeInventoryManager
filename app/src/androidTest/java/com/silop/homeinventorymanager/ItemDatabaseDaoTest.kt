package com.silop.homeinventorymanager

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ItemDatabaseDaoTest {

    private lateinit var dao: ItemDatabaseDao
    private lateinit var db: ItemDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, ItemDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.ItemDatabaseDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetItem() = runBlocking {
        val item = Item(
            name = "Jack Daniels",
            location = "Living Room",
            amount = 1,
            lastNeeded = "2023-02-02"
        )
        dao.insert(item)
        item.id = 1

        val queriedItem = dao.getAllItems().first().first()

        assertEquals(queriedItem, item)
    }

    @Test
    @Throws(Exception::class)
    fun insertSeveralGetByName() = runBlocking {
        val item1 = Item(
            name = "Jack Daniels",
            location = "Living Room",
            amount = 1,
            lastNeeded = "2023-02-02"
        )
        val item2 = Item(
            name = "Pillow",
            location = "Living Room",
            amount = 4,
            lastNeeded = "2023-02-02"
        )
        val item3 = Item(
            name = "Knife",
            location = "Kitchen",
            amount = 10,
            lastNeeded = "2023-02-02"
        )

        dao.insert(item1)
        dao.insert(item2)
        dao.insert(item3)

        item1.id = 1
        item2.id = 2
        item3.id = 3

        val queriedItem = dao.getName("Pillow").first().first()

        assertEquals(queriedItem, item2)
    }
}