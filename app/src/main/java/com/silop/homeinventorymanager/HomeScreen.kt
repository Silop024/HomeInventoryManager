package com.silop.homeinventorymanager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.silop.homeinventorymanager.ui.theme.HomeInventoryManagerTheme

val bottomRowHeight = 64.dp

@Composable
fun HomeView(viewModel: ItemViewModel) {
    val items by viewModel.items.collectAsState(emptyList())
    var searchQuery by remember { mutableStateOf("") }

    HomeInventoryManagerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .imePadding(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            ItemList(items, viewModel, searchQuery, Modifier.weight(1f, fill = false))
            Row(
                modifier = Modifier
                    .height(bottomRowHeight)
                    .fillMaxWidth()
                    .padding(start = 6.dp, end = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom
            ) {
                OutlinedTextField( // Search field
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search") },
                    singleLine = true,
                    modifier = Modifier.background(MaterialTheme.colors.background)
                )
                Spacer(Modifier.width(6.dp))
                AddButton(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ItemList(itemsList: List<Item>, viewModel: ItemViewModel, searchQuery: String, modifier: Modifier) {
    val itemsGroup = if (searchQuery != "") {
        itemsList
            .filter { it.name.contains(searchQuery, ignoreCase = true) }
            .sortedBy { it.name }
            .groupBy { it.location }
    } else {
        itemsList
            .sortedBy { it.name }
            .groupBy { it.location }
    }.values.toList()

    Column(
        modifier = modifier
            .heightIn(
                min = 0.dp,
                max = LocalConfiguration.current.screenHeightDp.dp - bottomRowHeight
            )
            .verticalScroll(rememberScrollState())
    ) {
        for (i in itemsGroup.indices) {
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
    var collapsed by remember { mutableStateOf(false) }

    Column(modifier = Modifier.clickable { collapsed = !collapsed }) {
        Text(
            text = itemsGroup[0].location,
            style = MaterialTheme.typography.h3,
            color = MaterialTheme.colors.onBackground,
            modifier = Modifier.padding(top = 3.dp, start = 12.dp, bottom = 3.dp)
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
                    .padding(start = 3.dp, end = 3.dp)
                    .background(color, shape = MaterialTheme.shapes.large)
                    .heightIn(max = 256.dp)
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
            .height(bottomRowHeight - 6.dp)
            .width(bottomRowHeight - 6.dp),
    ) {
        Text(
            text = "+",
            style = MaterialTheme.typography.button,
            color = MaterialTheme.colors.onSecondary
        )
    }

    if (showDialog) {
        ItemDialog(
            item = Item(name = "", location = "", amount = 1),
            titleText = "Add item",
            confirmText = "Add",
            onDismissRequest = { showDialog = false },
            onConfirm = { viewModel.addItem(it); showDialog = false }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
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

    var dropDownExpanded by remember { mutableStateOf(false) }
    val locations = listOf("Living Room", "Kitchen", "Closet", "Bathroom", "Basement")

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

                ExposedDropdownMenuBox(
                    expanded = dropDownExpanded,
                    onExpandedChange = { dropDownExpanded = !dropDownExpanded }
                ) {
                    OutlinedTextField(
                        value = location,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Location") }
                    )
                    ExposedDropdownMenu(
                        expanded = dropDownExpanded,
                        onDismissRequest = { dropDownExpanded = false }
                    ) {
                        locations.forEach {
                            DropdownMenuItem(
                                onClick = {
                                    location = it
                                    dropDownExpanded = false
                                }) {
                                Text(text = it)
                            }
                        }
                    }
                }

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