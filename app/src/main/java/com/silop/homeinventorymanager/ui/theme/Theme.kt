package com.silop.homeinventorymanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = googleBlue,
    primaryVariant = googleRed,
    secondary = googleGreen,
    secondaryVariant = googleYellow,
    background = white100,
    onPrimary = white100,
    onSecondary = white100,
    onBackground = googleText
)

@Composable
fun HomeInventoryManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = LightColorPalette


    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}