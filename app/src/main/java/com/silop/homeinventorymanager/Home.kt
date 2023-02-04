@file:OptIn(ExperimentalTextApi::class)

package com.silop.homeinventorymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.silop.homeinventorymanager.ui.theme.HomeInventoryManagerTheme

class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeInventoryManagerTheme {
                HomeView()
            }
        }
    }
}

@Composable
fun ItemRow(item: Item) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            modifier = Modifier.size(width = 100.dp, height = 25.dp),
            color = MaterialTheme.colors.surface
        )
        Text(
            text = "${item.amount}x",
            modifier = Modifier.size(width = 50.dp, height = 25.dp),
            color = MaterialTheme.colors.secondaryVariant
        )
        Text(
            text = item.lastNeeded,
            modifier = Modifier.size(width = 100.dp, height = 25.dp),
            color = MaterialTheme.colors.surface
        )
    }
}

@Composable
fun ItemGroup(inventory_items: List<Item>) {
    Column(
        modifier = Modifier.background(MaterialTheme.colors.primaryVariant, RoundedCornerShape(15))
    ) {
        Text(
            text = inventory_items.first().location,
            modifier = Modifier.padding(5.dp)
        )
        inventory_items.forEach { ItemRow(item = it)}
    }
}

@Composable
fun ItemList(items: List<Item>) {
    val rooms = items.sortedByDescending { it.lastNeeded }.groupBy { it.location }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(5.dp)
    ) {
        rooms.values.forEach { item { ItemGroup(inventory_items = it)} }
    }
}

@Composable
fun SearchField() {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = CircleShape
            )
            .width(250.dp),
        placeholder = { Text(text = "Search", color = MaterialTheme.colors.surface) },
        label = null,
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            backgroundColor = Color.Transparent,
            textColor = MaterialTheme.colors.background
        )
    )
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun HomeView() {
    val test = listOf(
        Item(0, "Banana", "Kitchen", 1, "2023-01-31"),
        Item(1, "Pineapple", "Kitchen", 1, "2023-02-01"),
        Item(2, "Cutie Patutie", "Bedroom", 1, "2023-02-02"),
        Item(3, "Paper", "Living Room", 1, "2023-02-01"),
        Item(4, "TV", "Living Room", 1, "2023-01-23")
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(bottom = 15.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        ItemList(items = test)
        Row(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            SearchField()
            Button(
                onClick = {},
                shape = CircleShape,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(50.dp)
            ) {
                Text(
                    text = "+",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center,
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ),
                        lineHeightStyle = LineHeightStyle(
                            alignment = LineHeightStyle.Alignment.Center,
                            trim = LineHeightStyle.Trim.None
                        ),
                        color = MaterialTheme.colors.surface
                    )
                )
            }
        }
    }
}
@Preview(name = "Home Preview", device = "id:One_Plus_Nord_2")
@Composable
fun DefaultPreview() {
    HomeInventoryManagerTheme {
        HomeView()
    }
}