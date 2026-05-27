package com.example.campusbites.features.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.campusbites.shared.theme.OrangeLight
import com.example.campusbites.shared.theme.OrangePrimary
import com.example.campusbites.shared.theme.TextDark
import com.example.campusbites.shared.theme.TextMuted

@Composable
fun HeroBanner(onViewMenuClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(OrangeLight)
            .padding(horizontal = 20.dp, vertical = 24.dp),
    ) {
        AsyncImage(
            model = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=700&h=400&fit=crop",
            contentDescription = "Delicious food",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
        )
        Spacer(Modifier.height(16.dp))
        Text(
            text = "Order your\nfavorite food here",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            lineHeight = 30.sp,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "Choose a diverse menu featuring a delectable array of dishes crafted with the finest ingredients and culinary expertise.",
            fontSize = 13.sp,
            color = TextMuted,
            lineHeight = 19.sp,
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onViewMenuClick,
            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text("View Menu", fontWeight = FontWeight.SemiBold)
        }
    }
}