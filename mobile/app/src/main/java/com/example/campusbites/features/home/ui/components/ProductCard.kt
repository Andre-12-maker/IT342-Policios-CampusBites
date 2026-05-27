package com.example.campusbites.features.home.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.campusbites.features.home.data.model.FOOD_IMAGES
import com.example.campusbites.features.home.data.model.Product
import com.example.campusbites.shared.theme.OrangePrimary
import com.example.campusbites.shared.theme.StarColor
import com.example.campusbites.shared.theme.TextDark
import com.example.campusbites.shared.theme.TextMuted
import com.example.campusbites.shared.theme.White
import kotlin.math.roundToInt

@Composable
fun ProductCard(
    product: Product,
    index: Int = 0,
    qty: Int = 0,
    onAdd: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
) {
    val imageUrl = if (product.image.isNullOrBlank())
        FOOD_IMAGES[index % FOOD_IMAGES.size]
    else product.image

    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {
            // ── Image + Add/Qty overlay ───────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(
                            RoundedCornerShape(
                                topStart = 12.dp,
                                topEnd = 12.dp,
                            )
                        ),
                )

                // Add / Qty control pinned bottom-right
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                ) {
                    if (qty == 0) {
                        Surface(
                            shape = CircleShape,
                            color = White,
                            shadowElevation = 4.dp,
                            modifier = Modifier.size(34.dp),
                        ) {
                            IconButton(
                                onClick = onAdd,
                                modifier = Modifier.fillMaxSize(),
                            ) {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Add",
                                    tint = OrangePrimary,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = OrangePrimary,
                            shadowElevation = 4.dp,
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(
                                    horizontal = 4.dp,
                                    vertical = 2.dp,
                                ),
                            ) {
                                IconButton(
                                    onClick = onDecrement,
                                    modifier = Modifier.size(26.dp),
                                ) {
                                    Icon(
                                        Icons.Default.Remove,
                                        contentDescription = "Minus",
                                        tint = White,
                                        modifier = Modifier.size(13.dp),
                                    )
                                }
                                Text(
                                    "$qty",
                                    color = White,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    modifier = Modifier.padding(horizontal = 4.dp),
                                )
                                IconButton(
                                    onClick = onIncrement,
                                    modifier = Modifier.size(26.dp),
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Plus",
                                        tint = White,
                                        modifier = Modifier.size(13.dp),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ── Card body ─────────────────────────────────────────────────
            Column(
                modifier = Modifier.padding(
                    horizontal = 10.dp,
                    vertical = 8.dp,
                ),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        product.name,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    StarRating(rating = product.rating)
                }
                Spacer(Modifier.height(4.dp))
                Text(
                    product.description,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 17.sp,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "\$${product.price.toInt()}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                )
            }
        }
    }
}

@Composable
private fun StarRating(rating: Double) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val full = rating.roundToInt()
        repeat(5) { i ->
            Icon(
                imageVector = if (i < full) Icons.Filled.Star
                else Icons.Outlined.StarOutline,
                contentDescription = null,
                tint = StarColor,
                modifier = Modifier.size(12.dp),
            )
        }
        Spacer(Modifier.width(3.dp))
        Text(
            "%.1f".format(rating),
            fontSize = 11.sp,
            color = TextMuted,
        )
    }
}