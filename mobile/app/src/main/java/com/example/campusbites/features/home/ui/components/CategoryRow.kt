package com.example.campusbites.features.home.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.campusbites.features.home.data.model.CATEGORIES
import com.example.campusbites.features.home.data.model.CATEGORY_IMAGES
import com.example.campusbites.shared.theme.DividerColor
import com.example.campusbites.shared.theme.OrangeLight
import com.example.campusbites.shared.theme.OrangePrimary
import com.example.campusbites.shared.theme.TextDark
import com.example.campusbites.shared.theme.TextMuted

@Composable
fun CategoryRow(
    selectedCategory: String,
    onCategorySelect: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp, vertical = 20.dp),
    ) {
        Text(
            "Explore our menu",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Choose a diverse menu featuring a delectable array of dishes crafted with the finest ingredients and culinary expertise.",
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 19.sp,
        )
        Spacer(Modifier.height(16.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(CATEGORIES) { cat ->
                val isSelected = cat == selectedCategory
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .width(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) OrangeLight else Color.Transparent
                        )
                        .border(
                            width = if (isSelected) 2.dp else 1.dp,
                            color = if (isSelected) OrangePrimary else DividerColor,
                            shape = RoundedCornerShape(12.dp),
                        )
                        .clickable { onCategorySelect(cat) }
                        .padding(vertical = 10.dp, horizontal = 4.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected)
                                    OrangePrimary.copy(alpha = 0.15f)
                                else
                                    Color(0xFFF3F4F6)
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        val imgUrl = CATEGORY_IMAGES[cat]
                        if (imgUrl != null) {
                            AsyncImage(
                                model = imgUrl,
                                contentDescription = cat,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                            )
                        } else {
                            // "All" category — simple grid icon
                            Column(
                                verticalArrangement = Arrangement.spacedBy(3.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Box(
                                        Modifier
                                            .size(10.dp)
                                            .background(
                                                if (isSelected) OrangePrimary else TextMuted,
                                                RoundedCornerShape(2.dp),
                                            )
                                    )
                                    Box(
                                        Modifier
                                            .size(10.dp)
                                            .background(
                                                if (isSelected) OrangePrimary else TextMuted,
                                                RoundedCornerShape(2.dp),
                                            )
                                    )
                                }
                                Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                                    Box(
                                        Modifier
                                            .size(10.dp)
                                            .background(
                                                if (isSelected) OrangePrimary else TextMuted,
                                                RoundedCornerShape(2.dp),
                                            )
                                    )
                                    Box(
                                        Modifier
                                            .size(10.dp)
                                            .background(
                                                if (isSelected) OrangePrimary else TextMuted,
                                                RoundedCornerShape(2.dp),
                                            )
                                    )
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        cat,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (isSelected) OrangePrimary else TextDark,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                    )
                }
            }
        }
    }
}