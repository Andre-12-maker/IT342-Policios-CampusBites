package com.example.campusbites.features.home.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campusbites.features.home.ui.components.CategoryRow
import com.example.campusbites.features.home.ui.components.HeroBanner
import com.example.campusbites.features.home.ui.components.ProductCard
import com.example.campusbites.features.home.ui.viewmodel.HomeUiState
import com.example.campusbites.features.home.ui.viewmodel.HomeViewModel
import com.example.campusbites.shared.theme.TextDark
import com.example.campusbites.shared.ui.components.CampusBitesNavBar

@Composable
fun HomeScreen(
    onCartClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState        by viewModel.uiState.collectAsState()
    val selectedCat    by viewModel.selectedCategory.collectAsState()
    val cartItems      by viewModel.cartItems.collectAsState()
    val cartCount      = cartItems.sumOf { it.qty }

    Scaffold(
        topBar = {
            CampusBitesNavBar(
                cartCount     = cartCount,
                onCartClick   = onCartClick,
                onSignInClick = onSignInClick,
                onSearchClick = {},
            )
        },
    ) { innerPadding ->

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(
                start  = 12.dp,
                end    = 12.dp,
                bottom = 24.dp,
            ),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement   = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {

            // ── Hero (full width) ─────────────────────────────────────────
            item(span = { GridItemSpan(2) }) {
                HeroBanner()
            }

            // ── Category row (full width) ─────────────────────────────────
            item(span = { GridItemSpan(2) }) {
                CategoryRow(
                    selectedCategory = selectedCat,
                    onCategorySelect = viewModel::selectCategory,
                )
            }

            // ── "Top dishes near you" header (full width) ─────────────────
            item(span = { GridItemSpan(2) }) {
                Text(
                    "Top dishes near you",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.padding(
                        horizontal = 4.dp,
                        vertical   = 4.dp,
                    ),
                )
            }

            // ── Product grid or states ────────────────────────────────────
            when (val state = uiState) {
                is HomeUiState.Loading -> {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is HomeUiState.Error -> {
                    item(span = { GridItemSpan(2) }) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                state.message,
                                color = MaterialTheme.colorScheme.error,
                            )
                        }
                    }
                }

                is HomeUiState.Success -> {
                    itemsIndexed(state.products) { index, product ->
                        val qty = viewModel.getQty(product.id)
                        ProductCard(
                            product    = product,
                            index      = index,
                            qty        = qty,
                            onAdd      = { viewModel.addToCart(product) },
                            onIncrement = {
                                viewModel.updateCartQty(product.id, qty + 1)
                            },
                            onDecrement = {
                                viewModel.updateCartQty(product.id, qty - 1)
                            },
                        )
                    }
                }
            }

            // ── Footer (full width) ───────────────────────────────────────
            item(span = { GridItemSpan(2) }) {
                HomeFooter()
            }
        }
    }
}

// ── Footer ────────────────────────────────────────────────────────────────────
@Composable
private fun HomeFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF3D3D3D))
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "C·BITES.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = com.example.campusbites.shared.theme.OrangePrimary,
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "Lorem ipsum is a dummy or placeholder text commonly used in graphic design, publishing, and web development.",
                    fontSize = 10.sp,
                    color = Color(0xFFBBBBBB),
                    lineHeight = 15.sp,
                )
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "COMPANY",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Spacer(Modifier.height(6.dp))
                listOf("Home", "About us", "Delivery", "Privacy policy").forEach {
                    Text(it, fontSize = 10.sp, color = Color(0xFFBBBBBB))
                    Spacer(Modifier.height(3.dp))
                }
            }
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    "GET IN TOUCH",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Spacer(Modifier.height(6.dp))
                Text("+1-212-456-7890",    fontSize = 10.sp, color = Color(0xFFBBBBBB))
                Spacer(Modifier.height(3.dp))
                Text("contact@cbites.com", fontSize = 10.sp, color = Color(0xFFBBBBBB))
            }
        }
        Spacer(Modifier.height(14.dp))
        HorizontalDivider(color = Color(0xFF555555))
        Spacer(Modifier.height(8.dp))
        Text(
            "Copyright 2024 © CBites.com – All Rights Reserved.",
            fontSize = 10.sp,
            color = Color(0xFF888888),
        )
    }
}