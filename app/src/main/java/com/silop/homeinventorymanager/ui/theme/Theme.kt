package com.silop.homeinventorymanager.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = Silver,
    primaryVariant = Alta,
    secondary = Glandis,
    secondaryVariant = VividTangerine,
    background = EbonyClay,
    surface = EastBay,

    onPrimary = EastBay
)

private val LightColorPalette = lightColors(
    primary = Silver,
    primaryVariant = Alta,
    secondary = Glandis,
    secondaryVariant = VividTangerine,
    background = EbonyClay,
    surface = EastBay,

    onPrimary = EastBay
)

@Composable
fun HomeInventoryManagerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}