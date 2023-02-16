@file:OptIn(ExperimentalTextApi::class)

package com.silop.homeinventorymanager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

@Composable
fun ItemRow(modifier: Modifier, item: Item) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { println("Item Row clicked") },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            modifier = modifier.size(width = 100.dp, height = 25.dp),
            color = MaterialTheme.colors.surface
        )
        Text(
            text = "${item.amount}x",
            modifier = modifier.size(width = 50.dp, height = 25.dp),
            color = MaterialTheme.colors.secondaryVariant
        )
        Text(
            text = item.lastNeeded,
            modifier = modifier.size(width = 100.dp, height = 25.dp),
            color = MaterialTheme.colors.surface
        )
    }
}

@Composable
fun ItemGroup(modifier: Modifier, inventory_items: List<Item>) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colors.primaryVariant,
                shape = RoundedCornerShape(15)
            )
            .clickable { println("Item Group clicked") }
    ) {
        Text(
            text = inventory_items.first().location,
            modifier = modifier.padding(start = 10.dp, top = 5.dp, bottom = 5.dp),
            color = MaterialTheme.colors.background
        )
        inventory_items.forEach { ItemRow(modifier = modifier, item = it) }
    }
}

@Composable
fun ItemList(modifier: Modifier, items: List<Item>) {
    val rooms = items.sortedByDescending { it.lastNeeded }.groupBy { it.location }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(5.dp)
    ) {
        rooms.values.forEach { item { ItemGroup(modifier = modifier, inventory_items = it)} }
    }
}

@Composable
fun SearchField(modifier: Modifier) {
    var text by remember { mutableStateOf(TextFieldValue("")) }
    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = CircleShape
            )
            .width(250.dp),
        placeholder = { Text(text = "Search", color = MaterialTheme.colors.onPrimary) },
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
fun AddItemButton(modifier: Modifier, viewModel: ItemViewModel) {
    Button(
        onClick = {
            viewModel.addItem(Item(name = "Phone", location = "Bed", amount = 1, lastNeeded = "Today"))
        },
        shape = CircleShape,
        modifier = modifier
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
                color = MaterialTheme.colors.onPrimary
            )
        )
    }
}

@Composable
fun HomeView(modifier: Modifier = Modifier, viewModel: ItemViewModel) {
    val itemsList = viewModel.items.collectAsState(emptyList()).value
    Surface {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(bottom = 15.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ItemList(modifier, itemsList)
            Row(
                modifier = modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SearchField(modifier)
                AddItemButton(modifier, viewModel)
            }
        }
    }
}
@Preview(name = "Home Preview", device = "id:One_Plus_Nord_2")
@Composable
fun DefaultPreview() {
    val items = listOf(
        Item(0, "Banana", "Kitchen", 1, "2023-01-31"),
        Item(1, "Pineapple", "Kitchen", 1, "2023-02-01"),
        Item(2, "Cutie Patutie", "Bedroom", 1, "2023-02-02"),
        Item(3, "Paper", "Living Room", 1, "2023-02-01"),
        Item(4, "TV", "Living Room", 1, "2023-01-23")
    )
    val modifier = Modifier
    HomeInventoryManagerTheme {
        Surface {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(bottom = 15.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                ItemList(modifier, items)
                Row(
                    modifier = modifier
                        .height(50.dp)
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    SearchField(modifier)
                    //AddItemButton(modifier)
                }
            }
        }
    }
}