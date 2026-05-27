package com.example.campusbites.shared.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val CampusBitesTypography = Typography(
    headlineLarge  = TextStyle(fontSize = 26.sp, fontWeight = FontWeight.Bold,     color = TextDark),
    headlineMedium = TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold,     color = TextDark),
    titleLarge     = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = TextDark),
    titleMedium    = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = TextDark),
    bodyLarge      = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal,   color = TextDark),
    bodyMedium     = TextStyle(fontSize = 13.sp, fontWeight = FontWeight.Normal,   color = TextMuted),
    labelMedium    = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium,   color = TextDark),
)