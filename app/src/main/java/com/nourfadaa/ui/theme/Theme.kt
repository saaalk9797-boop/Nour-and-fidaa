package com.nourfadaa.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object NourColors {
    val Cream = Color(0xFFFFF8EC)
    val Teal = Color(0xFF2E9E8F)
    val Gold = Color(0xFFF6B93B)
    val Pink = Color(0xFFEF7F9B)
    val Lavender = Color(0xFF7C83FD)
    val Ink = Color(0xFF2B2D42)
    val Tiles = listOf(
        Color(0xFFFFD6E0), Color(0xFFD6F0EC), Color(0xFFFFEBC2),
        Color(0xFFE0E2FF), Color(0xFFD9F2D0)
    )
}

private val scheme = lightColorScheme(
    primary = NourColors.Teal,
    secondary = NourColors.Gold,
    tertiary = NourColors.Pink,
    background = NourColors.Cream,
    surface = Color.White,
    onBackground = NourColors.Ink,
    onSurface = NourColors.Ink
)

@Composable
fun NourTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = scheme, content = content)
}
