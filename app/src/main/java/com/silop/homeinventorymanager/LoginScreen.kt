package com.silop.homeinventorymanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.silop.homeinventorymanager.restapi.itemApi
import kotlinx.coroutines.launch

@Composable
fun LoginView(navController: NavHostController) {
    val coroutineScope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var secret by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Log in")

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") }
        )

        OutlinedTextField(
            value = secret,
            onValueChange = { secret = it },
            label = { Text("Name") }
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    if (logIn(name, secret)) {
                        navController.navigate(Screen.MainScreen.route)
                    }
                }
            }
        ) {
            Text("Log in")
        }
    }
}


suspend fun logIn(name: String, secret: String): Boolean {
    return itemApi.login(User(name, secret))
}