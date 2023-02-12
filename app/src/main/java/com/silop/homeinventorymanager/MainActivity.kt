package com.silop.homeinventorymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.silop.homeinventorymanager.ui.theme.HomeInventoryManagerTheme

class MainActivity : ComponentActivity() {
    private val itemViewModel: ItemViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeInventoryManagerTheme {
                HomeView(viewModel = itemViewModel)
            }
        }
    }
}