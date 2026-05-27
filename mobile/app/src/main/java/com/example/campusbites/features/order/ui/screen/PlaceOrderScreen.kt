package com.example.campusbites.features.order.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.campusbites.features.cart.ui.viewmodel.CartViewModel
import com.example.campusbites.features.cart.ui.viewmodel.OrderUiState
import com.example.campusbites.shared.theme.OrangePrimary
import com.example.campusbites.shared.theme.SurfaceGray
import com.example.campusbites.shared.theme.TextDark
import com.example.campusbites.shared.theme.TextMuted
import com.example.campusbites.shared.ui.components.CampusBitesNavBar

@Composable
fun PlaceOrderScreen(
    onOrderSuccess: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    viewModel: CartViewModel = hiltViewModel(),
) {
    val orderState by viewModel.orderState.collectAsState()

    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var street    by remember { mutableStateOf("") }
    var city      by remember { mutableStateOf("") }
    var state     by remember { mutableStateOf("") }
    var zipCode   by remember { mutableStateOf("") }
    var country   by remember { mutableStateOf("") }
    var phone     by remember { mutableStateOf("") }
    var promoCode by remember { mutableStateOf("") }

    LaunchedEffect(orderState) {
        if (orderState is OrderUiState.Success) {
            viewModel.resetOrderState()
            onOrderSuccess()
        }
    }

    Scaffold(
        topBar = {
            CampusBitesNavBar(
                cartCount = 0,
                onSignInClick = onSignInClick,
            )
        },
    ) { innerPadding ->

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // ── Left: Delivery form ───────────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1.4f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                Text(
                    "Delivery Information",
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = TextDark,
                )
                Spacer(Modifier.height(12.dp))

                // First / Last name row
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField(
                        value = firstName,
                        onValueChange = { firstName = it },
                        placeholder = "First name",
                        modifier = Modifier.weight(1f),
                    )
                    FormField(
                        value = lastName,
                        onValueChange = { lastName = it },
                        placeholder = "Last name",
                        modifier = Modifier.weight(1f),
                    )
                }
                Spacer(Modifier.height(8.dp))
                FormField(email, { email = it }, "Email address",
                    keyboardType = KeyboardType.Email)
                Spacer(Modifier.height(8.dp))
                FormField(street, { street = it }, "Street")
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField(city,  { city  = it }, "City",  modifier = Modifier.weight(1f))
                    FormField(state, { state = it }, "State", modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FormField(zipCode, { zipCode = it }, "Zip code",
                        keyboardType = KeyboardType.Number, modifier = Modifier.weight(1f))
                    FormField(country, { country = it }, "Country",
                        modifier = Modifier.weight(1f))
                }
                Spacer(Modifier.height(8.dp))
                FormField(phone, { phone = it }, "Phone",
                    keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done)

                Spacer(Modifier.height(16.dp))
                Text(
                    "If you have a promo code, Enter it here",
                    fontSize = 12.sp,
                    color = TextMuted,
                )
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = promoCode,
                        onValueChange = { promoCode = it },
                        placeholder = { Text("promo code", fontSize = 13.sp) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
                    )
                    Button(
                        onClick = { /* apply promo */ },
                        colors = ButtonDefaults.buttonColors(containerColor = TextDark),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(56.dp),
                    ) {
                        Text("Submit", fontSize = 13.sp)
                    }
                }
            }

            // ── Right: Cart totals ────────────────────────────────────────
            Card(
                modifier = Modifier
                    .weight(1f)
                    .wrapContentHeight(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceGray),
                elevation = CardDefaults.cardElevation(2.dp),
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Cart Totals",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextDark,
                    )
                    Spacer(Modifier.height(12.dp))
                    TotalRow("Subtotal",     "$${viewModel.subtotal.toInt()}")
                    TotalRow("Delivery fee", "$${viewModel.deliveryFee.toInt()}")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    TotalRow("Total",        "$${viewModel.total.toInt()}", bold = true)
                    Spacer(Modifier.height(16.dp))

                    if (orderState is OrderUiState.Error) {
                        Text(
                            (orderState as OrderUiState.Error).message,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp,
                        )
                        Spacer(Modifier.height(8.dp))
                    }

                    Button(
                        onClick = {
                            viewModel.placeOrder(
                                firstName, lastName, email,
                                street, city, state,
                                zipCode, country, phone,
                                promoCode,
                            )
                        },
                        enabled = orderState !is OrderUiState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        if (orderState is OrderUiState.Loading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                            )
                        } else {
                            Text(
                                "PROCEED TO PAYMENT",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
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
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, fontSize = 13.sp, color = TextMuted) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction,
        ),
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
    )
}

@Composable
private fun TotalRow(label: String, value: String, bold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            label, fontSize = 13.sp,
            color = if (bold) TextDark else TextMuted,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        )
        Text(
            value, fontSize = 13.sp, color = TextDark,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        )
    }
}