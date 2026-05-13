package com.example.foodorderingapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val OrangePrimary = Color(0xFFFF6B35)
val OrangeLight = Color(0xFFFF8C61)
val OrangeDark = Color(0xFFE84A00)
val Background = Color(0xFFFFF8F5)
val SurfaceColor = Color(0xFFFFFFFF)
val CardColor = Color(0xFFFFF3EE)
val TextPrimary = Color(0xFF1A1A1A)
val TextSecondary = Color(0xFF6B6B6B)
val TextHint = Color(0xFFAAAAAA)
val GreenSuccess = Color(0xFF4CAF50)
val RedError = Color(0xFFF44336)

private val LightColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFE0D0),
    onPrimaryContainer = OrangeDark,
    secondary = Color(0xFF8D5524),
    onSecondary = Color.White,
    background = Background,
    surface = SurfaceColor,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = RedError,
    onError = Color.White
)

@Composable
fun FoodOrderingAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        content = content
    )
}
