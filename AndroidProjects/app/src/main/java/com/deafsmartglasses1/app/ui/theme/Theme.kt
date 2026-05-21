package com.deafsmartglasses1.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColors = darkColorScheme(
    primary = AppWhite,
    onPrimary = AppBlack,
    secondary = AppOrange,
    background = AppBlack,
    onBackground = AppWhite,
    surface = AppSurface,
    onSurface = AppWhite,
    error = AppRed
)

@Composable
fun DeafSmartGlassesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = Typography,
        content = content
    )
}
