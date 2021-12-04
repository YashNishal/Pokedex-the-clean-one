package com.example.pokedex.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = headingColor,
    background = darkBackground,
    onBackground = Color.White,
    surface = darkBackground,
    onSurface = Color.White,
    secondary = offBackground,
    onPrimary = Color.White
)

private val LightColorPalette = lightColors(
    primary = headingColor,
    background = background,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = headingColor,
    secondary = subheadingColor,
    onPrimary = Color.White
)


@Composable
fun PokeDexTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
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



