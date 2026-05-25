package com.carmusic.player.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val CarColorScheme = darkColorScheme(
    primary = ThemeBlue,
    onPrimary = TextPrimary,
    secondary = TextSecondary,
    background = BgMain,
    surface = BgCard,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
)

@Composable
fun CarMusicTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CarColorScheme,
        content = content
    )
}
