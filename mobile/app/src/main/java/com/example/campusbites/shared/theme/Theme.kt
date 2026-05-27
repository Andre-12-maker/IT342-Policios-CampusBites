package com.example.campusbites.shared.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val OrangePrimary = Color(0xFFE8741A)
val OrangeDark    = Color(0xFFBF5A10)
val OrangeLight   = Color(0xFFFFF3E8)
val TextDark      = Color(0xFF1A1A1A)
val TextMuted     = Color(0xFF6B7280)
val SurfaceGray   = Color(0xFFF9FAFB)
val DividerColor  = Color(0xFFE5E7EB)
val StarColor     = Color(0xFFFBBF24)
val White         = Color(0xFFFFFFFF)

private val LightColors = lightColorScheme(
    primary           = OrangePrimary,
    onPrimary         = White,
    primaryContainer  = OrangeLight,
    secondary         = TextMuted,
    background        = White,
    surface           = White,
    surfaceVariant    = SurfaceGray,
    onBackground      = TextDark,
    onSurface         = TextDark,
    outline           = DividerColor,
)

@Composable
fun CampusBitesTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography  = CampusBitesTypography,
        content     = content,
    )
}