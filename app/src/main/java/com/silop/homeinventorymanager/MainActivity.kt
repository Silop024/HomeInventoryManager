package com.silop.homeinventorymanager

import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val test = listOf(
            Item(0, "Banana", "Livingroom", "2023-01-31"),
            Item(1, "Pinapple", "Livingroom", "2023-02-01"),
            Item(2, "Cudez", "Livingroom", "Never"),
            Item(3, "Paper", "Livingroom", "2023-02-01"),
            Item(4, "TV", "Livingroom", "2023-01-23")
        )
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                ItemList(items = test)
            }
        }
    }
}

@Composable
fun ItemRow(item: Item) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = item.name)
        Text(text = item.location)
        Text(text = item.last_needed)
    }
}

@Composable
fun ItemList(items: List<Item>) {
    Column(
        modifier = Modifier.verticalScroll(
            state = ScrollState(0),
            enabled = true,
            reverseScrolling = false,
        )
    ) {
        items.forEach { ItemRow(item = it) }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {

}