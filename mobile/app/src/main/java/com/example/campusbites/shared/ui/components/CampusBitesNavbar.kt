package com.example.campusbites.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campusbites.shared.theme.OrangePrimary
import com.example.campusbites.shared.theme.TextDark

@Composable
fun CampusBitesNavBar(
    cartCount: Int = 0,
    onCartClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
) {
    Surface(
        shadowElevation = 4.dp,
        color = MaterialTheme.colorScheme.background,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            // ── Logo ──────────────────────────────────────────────────────
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(OrangePrimary),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        "C",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    "BITES.",
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = TextDark,
                        fontWeight = FontWeight.ExtraBold,
                    ),
                )
            }

            // ── Right actions ─────────────────────────────────────────────
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        tint = TextDark,
                    )
                }
                BadgedBox(
                    badge = {
                        if (cartCount > 0) {
                            Badge(containerColor = OrangePrimary) {
                                Text(
                                    "$cartCount",
                                    fontSize = 10.sp,
                                    color = Color.White,
                                )
                            }
                        }
                    }
                ) {
                    IconButton(onClick = onCartClick) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = TextDark,
                        )
                    }
                }
                OutlinedButton(
                    onClick = onSignInClick,
                    contentPadding = PaddingValues(
                        horizontal = 10.dp,
                        vertical   = 4.dp,
                    ),
                    modifier = Modifier.height(32.dp),
                ) {
                    Text("sign in", fontSize = 12.sp)
                }
            }
        }
    }
}