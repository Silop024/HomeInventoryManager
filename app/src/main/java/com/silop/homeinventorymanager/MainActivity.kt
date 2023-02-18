package com.silop.homeinventorymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.silop.homeinventorymanager.ui.theme.HomeInventoryManagerTheme
import java.util.*

val bottomRowHeight = 64.dp

class MainActivity : ComponentActivity() {
    private val itemViewModel: ItemViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffect(Unit) {
                try {
                    itemViewModel.loadItems()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            HomeView(viewModel = itemViewModel)
        }
    }
}

@Composable
fun HomeView(viewModel: ItemViewModel) {
    val items by viewModel.items.collectAsState(emptyList())

    HomeInventoryManagerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
        ) {
            ItemList(items, viewModel, Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .height(bottomRowHeight)
                    .fillMaxWidth()
            ) {
                AddButton(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ItemList(itemsList: List<Item>, viewModel: ItemViewModel, modifier: Modifier) {
    val itemsGroupMap = itemsList.sortedByDescending { it.lastNeeded }.groupBy { it.location }

    val itemsGroup = mutableListOf<List<Item>>()

    for (group in itemsGroupMap.values) {
        itemsGroup.add(group)
    }

    Column {
        for (i in 0 until itemsGroup.size) {
            ItemGroup(
                itemsGroup = itemsGroup[i], viewModel = viewModel, color = when (i % 4) {
                    0 -> MaterialTheme.colors.primary
                    1 -> MaterialTheme.colors.primaryVariant
                    2 -> MaterialTheme.colors.secondaryVariant
                    else -> MaterialTheme.colors.secondary
                }
            )
        }
    }
}

@Composable
fun ItemGroup(itemsGroup: List<Item>, viewModel: ItemViewModel, color: Color) {
    var collapsed by remember { mutableStateOf(true) }

    Column(modifier = Modifier.clickable { collapsed = !collapsed }) {
        Text(
            text = itemsGroup[0].location,
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(top = 3.dp, start = 12.dp)
        )

        if (collapsed) {
            Row(
                modifier = Modifier
                    .padding()
                    .height(32.dp)
                    .background(color, shape = MaterialTheme.shapes.medium)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Expand",
                    style = MaterialTheme.typography.caption,
                    color = MaterialTheme.colors.onPrimary
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(top = 3.dp, start = 3.dp, end = 3.dp)
                    .background(color, shape = MaterialTheme.shapes.large)
            ) {
                items(items = itemsGroup) { item ->
                    ItemRow(item = item, viewModel)
                }
            }
        }
    }
}

@Composable
fun ItemRow(item: Item, viewModel: ItemViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(32.dp)
            .clickable { showDialog = true },
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            modifier = Modifier
                .fillMaxHeight()
                .width(160.dp),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onPrimary
        )
        Text(
            text = "${item.amount}x",
            modifier = Modifier
                .fillMaxHeight()
                .width(80.dp),
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onPrimary
        )
    }

    if (showDialog) {
        ItemDialog(
            item = item,
            titleText = "Edit item",
            confirmText = "Edit",
            onDismissRequest = { showDialog = false },
            onDelete = { viewModel.removeItem(item); showDialog = false },
            onConfirm = { viewModel.updateItem(it); showDialog = false }
        )
    }
}

@Composable
fun AddButton(viewModel: ItemViewModel) {
    var showDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showDialog = true },
        shape = CircleShape,
        modifier = Modifier
            .height(bottomRowHeight)
            .width(bottomRowHeight),
    ) {
        Text(
            text = "+",
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSecondary
        )
    }

    if (showDialog) {
        ItemDialog(item = Item(
            name = "", location = "", amount = 1, lastNeeded = Date().toString()
        ),
            titleText = "Add item",
            confirmText = "Add",
            onDismissRequest = { showDialog = false },
            onConfirm = { viewModel.addItem(it); showDialog = false }
        )
    }
}

@Composable
fun ItemDialog(
    item: Item,
    titleText: String,
    confirmText: String,
    onDismissRequest: () -> Unit,
    onDelete: () -> Unit = {},
    onConfirm: (Item) -> Unit
) {
    var name by remember { mutableStateOf(item.name) }
    var location by remember { mutableStateOf(item.location) }
    var amount by remember { mutableStateOf(item.amount.toString()) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(titleText) },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                OutlinedTextField(
                    value = location,
                    onValueChange = { location = it },
                    label = { Text("Location") }
                )
                OutlinedTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    label = { Text("Amount") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
            }
        },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp, start = 6.dp, end = 6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                var fontSize = MaterialTheme.typography.button.fontSize
                if (titleText == "Edit item") { // Delete button
                    fontSize = MaterialTheme.typography.h3.fontSize

                    Button(onClick = onDelete, modifier = Modifier.weight(1f)) {
                        Text("Delete", fontSize = fontSize)
                    }
                    Spacer(Modifier.width(6.dp))
                }
                Button(
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f)
                ) { // Dismiss button
                    Text("Cancel", fontSize = fontSize)
                }
                Spacer(Modifier.width(6.dp))
                Button( // Confirm button
                    onClick = {
                        item.name = name
                        item.location = location
                        item.amount = amount.toInt()
                        onConfirm(item)
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text(confirmText, fontSize = fontSize)
                }
            }
        },
        shape = MaterialTheme.shapes.large
    )
}