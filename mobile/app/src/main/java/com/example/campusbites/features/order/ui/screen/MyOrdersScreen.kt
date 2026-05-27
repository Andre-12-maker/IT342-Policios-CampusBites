package com.example.campusbites.features.order.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campusbites.features.order.data.model.OrderResponse
import com.example.campusbites.features.order.ui.viewmodel.MyOrdersUiState
import com.example.campusbites.features.order.ui.viewmodel.MyOrdersViewModel
import com.example.campusbites.shared.theme.*
import com.example.campusbites.shared.ui.components.CampusBitesNavBar

@Composable
fun MyOrdersScreen(
    onSignInClick: () -> Unit = {},
    viewModel: MyOrdersViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            CampusBitesNavBar(
                onSignInClick = onSignInClick,
            )
        },
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
        ) {
            Text(
                "My Orders",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextDark,
            )
            Spacer(Modifier.height(16.dp))

            when (val s = state) {
                is MyOrdersUiState.Loading -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                is MyOrdersUiState.Error -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(s.message, color = TextMuted, fontSize = 14.sp)
                            Spacer(Modifier.height(12.dp))
                            OutlinedButton(onClick = viewModel::load) { Text("Retry") }
                        }
                    }
                }
                is MyOrdersUiState.Success -> {
                    if (s.orders.isEmpty()) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No orders yet", color = TextMuted, fontSize = 16.sp)
                        }
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(s.orders, key = { it.id }) { order ->
                                OrderCard(order = order)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OrderCard(order: OrderResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        border = CardDefaults.outlinedCardBorder(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // Package icon placeholder
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(OrangeLight),
                contentAlignment = Alignment.Center,
            ) {
                Text("📦", fontSize = 24.sp)
            }

            // Order details
            Column(modifier = Modifier.weight(1f)) {
                // Item names summary
                val summary = order.items.joinToString(", ") {
                    "${it.name} x ${it.quantity}"
                }.ifBlank { "Order #${order.id.takeLast(6)}" }

                Text(
                    summary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextDark,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "$${order.amount.toInt()}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    "Items: ${order.items.sumOf { it.quantity }}",
                    fontSize = 12.sp,
                    color = TextMuted,
                )
            }

            // Status + Track button
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                // Status dot + label
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(
                                when (order.status.lowercase()) {
                                    "delivered" -> Color(0xFF22C55E)
                                    "cancelled" -> Color(0xFFEF4444)
                                    else        -> OrangePrimary
                                }
                            ),
                    )
                    Text(
                        order.status,
                        fontSize = 11.sp,
                        color = TextMuted,
                    )
                }

                Button(
                    onClick = { /* track order */ },
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    modifier = Modifier.height(30.dp),
                ) {
                    Text("Track Order", fontSize = 11.sp, color = Color.White)
                }
            }
        }
    }
}