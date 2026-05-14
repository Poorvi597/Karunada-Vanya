package com.karunadavanya.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val KarunadaColors = lightColorScheme(
    primary = Color(0xFF114B3C),
    onPrimary = Color.White,
    secondary = Color(0xFFC96C3B),
    onSecondary = Color.White,
    tertiary = Color(0xFF356A8A),
    background = Color(0xFFF7F3EA),
    onBackground = Color(0xFF17211D),
    surface = Color(0xFFFFFCF4),
    onSurface = Color(0xFF17211D),
    surfaceVariant = Color(0xFFE4DDD1),
    onSurfaceVariant = Color(0xFF4C514A),
    error = Color(0xFFBA1A1A)
)

@Composable
fun KarunadaVanyaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = KarunadaColors,
        typography = MaterialTheme.typography,
        content = content
    )
}
