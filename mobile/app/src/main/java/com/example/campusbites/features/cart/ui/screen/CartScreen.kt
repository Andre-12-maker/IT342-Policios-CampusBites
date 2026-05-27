package com.example.campusbites.features.cart.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.campusbites.features.cart.ui.viewmodel.CartViewModel
import com.example.campusbites.features.home.data.model.FOOD_IMAGES
import com.example.campusbites.shared.theme.*
import com.example.campusbites.shared.ui.components.CampusBitesNavBar

@Composable
fun CartScreen(
    onCheckout: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    viewModel: CartViewModel = hiltViewModel(),
) {
    val items by viewModel.cartItems.collectAsState()

    Scaffold(
        topBar = {
            CampusBitesNavBar(
                cartCount = items.sumOf { it.qty },
                onCartClick = { /* already here */ },
                onSignInClick = onSignInClick,
            )
        },
    ) { innerPadding ->

        if (items.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center,
            ) {
                Text("Your cart is empty", color = TextMuted, fontSize = 16.sp)
            }
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {

            // ── Header row ────────────────────────────────────────────────
            item {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text("Items",    modifier = Modifier.weight(1.2f), fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextMuted)
                    Text("Title",   modifier = Modifier.weight(2f),   fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextMuted)
                    Text("Price",   modifier = Modifier.weight(1f),   fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextMuted)
                    Text("Qty",     modifier = Modifier.weight(1.5f), fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextMuted)
                    Text("Total",   modifier = Modifier.weight(1f),   fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextMuted)
                    Text("Remove",  modifier = Modifier.weight(1f),   fontWeight = FontWeight.SemiBold, fontSize = 13.sp, color = TextMuted)
                }
                HorizontalDivider(modifier = Modifier.padding(top = 8.dp))
            }

            // ── Cart items ────────────────────────────────────────────────
            items(items, key = { it.product.id }) { item ->
                val imgUrl = item.product.image?.takeIf { it.isNotBlank() }
                    ?: FOOD_IMAGES[item.product.id.hashCode().and(0x7fffffff) % FOOD_IMAGES.size]

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    // Image
                    AsyncImage(
                        model = imgUrl,
                        contentDescription = item.product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .weight(1.2f)
                            .size(48.dp)
                            .clip(RoundedCornerShape(8.dp)),
                    )
                    // Name
                    Text(
                        item.product.name,
                        modifier = Modifier
                            .weight(2f)
                            .padding(horizontal = 4.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextDark,
                    )
                    // Price
                    Text(
                        "$${item.product.price.toInt()}",
                        modifier = Modifier.weight(1f),
                        fontSize = 13.sp,
                        color = TextDark,
                    )
                    // Qty stepper
                    Row(
                        modifier = Modifier.weight(1.5f),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Box(
                            Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .run { this },
                            contentAlignment = Alignment.Center,
                        ) {
                            IconButton(
                                onClick = { viewModel.decrement(item.product.id) },
                                modifier = Modifier.size(22.dp),
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "−",
                                    tint = OrangePrimary, modifier = Modifier.size(14.dp))
                            }
                        }
                        Text("${item.qty}", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Box(
                            Modifier
                                .size(22.dp)
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center,
                        ) {
                            IconButton(
                                onClick = { viewModel.increment(item.product.id) },
                                modifier = Modifier.size(22.dp),
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "+",
                                    tint = OrangePrimary, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                    // Row total
                    Text(
                        "$${(item.product.price * item.qty).toInt()}",
                        modifier = Modifier.weight(1f),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark,
                    )
                    // Remove
                    IconButton(
                        onClick = { viewModel.remove(item.product.id) },
                        modifier = Modifier
                            .weight(1f)
                            .size(28.dp),
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Remove",
                            tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
                HorizontalDivider()
            }

            // ── Cart Totals card ──────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceGray),
                    elevation = CardDefaults.cardElevation(2.dp),
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Cart Totals",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextDark,
                        )
                        Spacer(Modifier.height(12.dp))
                        TotalRow("Subtotal", "$${viewModel.subtotal.toInt()}")
                        TotalRow("Delivery fee", "$${viewModel.deliveryFee.toInt()}")
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                        TotalRow("Total", "$${viewModel.total.toInt()}", bold = true)
                        Spacer(Modifier.height(16.dp))
                        Button(
                            onClick = onCheckout,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            Text(
                                "PROCEED TO CHECKOUT",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color.White,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TotalRow(label: String, value: String, bold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(label, fontSize = 14.sp, color = if (bold) TextDark else TextMuted,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
        Text(value, fontSize = 14.sp, color = TextDark,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal)
    }
}