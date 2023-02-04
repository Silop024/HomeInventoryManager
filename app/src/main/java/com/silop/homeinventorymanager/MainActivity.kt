package com.silop.homeinventorymanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.silop.homeinventorymanager.ui.theme.HomeInventoryManagerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeInventoryManagerTheme {
                HomeView()
            }
        }
    }
}