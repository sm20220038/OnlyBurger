package com.onlyburger.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val OnlyBurgerColors = lightColorScheme(
    primary = ObRed,
    onPrimary = Color.White,
    secondary = ObYellow,
    onSecondary = Color(0xFF4A3500),
    background = ObBackground,
    onBackground = ObTextDark,
    surface = ObSurface,
    onSurface = ObTextDark,
    surfaceVariant = Color(0xFFEFEFEC),
    onSurfaceVariant = ObMuted,
    error = Color(0xFFD23B3B),
    onError = Color.White,
)

/**
 * App theme. A single light color scheme keyed off the OnlyBurger brand (red + yellow),
 * intentionally not following the system dark theme to keep the fast-food look consistent.
 */
@Composable
fun OnlyBurgerTheme(
    @Suppress("UNUSED_PARAMETER") darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = OnlyBurgerColors,
        typography = AppTypography,
        content = content,
    )
}
