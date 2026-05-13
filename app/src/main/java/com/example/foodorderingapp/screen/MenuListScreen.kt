package com.example.foodorderingapp.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodorderingapp.model.FoodItem
import com.example.foodorderingapp.ui.theme.*
import com.example.foodorderingapp.viewmodel.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuListScreen(
    viewModel: OrderViewModel,
    onItemClick: (Int) -> Unit
) {
    var showOrderSummary by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            listOf(OrangePrimary, OrangeLight)
                        )
                    )
            ) {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "🍽️ Food Order",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                color = Color.White
                            )
                            Text(
                                text = "Pilih makanan favoritmu",
                                fontSize = 12.sp,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    },
                    actions = {
                        Box(modifier = Modifier.padding(end = 12.dp)) {
                            IconButton(
                                onClick = { showOrderSummary = !showOrderSummary }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Order Summary",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }
                            if (viewModel.totalItems > 0) {
                                Badge(
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    containerColor = Color.White,
                                    contentColor = OrangePrimary
                                ) {
                                    Text(
                                        text = viewModel.totalItems.toString(),
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            }
        },
        containerColor = CreamBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Order Summary Panel (collapsible)
            AnimatedVisibility(
                visible = showOrderSummary,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                OrderSummaryPanel(viewModel = viewModel)
            }

            // Menu List
            if (viewModel.menuItems.isEmpty()) {
                EmptyMenuState()
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.menuItems, key = { it.id }) { item ->
                        MenuItemCard(
                            item = item,
                            onItemClick = { onItemClick(item.id) },
                            onIncrease = { viewModel.increaseQuantity(item.id) },
                            onDecrease = { viewModel.decreaseQuantity(item.id) },
                            formatPrice = viewModel::formatPrice
                        )
                    }

                    // Bottom empty state
                    item {
                        if (viewModel.totalItems == 0) {
                            EmptyOrderState()
                        }
                    }

                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
fun OrderSummaryPanel(viewModel: OrderViewModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🛒 Ringkasan Pesanan",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.totalItems == 0) {
                Text(
                    text = "No orders yet",
                    color = TextHint,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.CenterHorizontally)
                        .padding(vertical = 8.dp)
                )
            } else {
                // Ordered items detail
                viewModel.menuItems
                    .filter { it.quantity > 0 }
                    .forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 3.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${item.emoji} ${item.name}",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "x${item.quantity}",
                                color = OrangePrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            Text(
                                text = viewModel.formatPrice(item.price * item.quantity),
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                Divider(
                    modifier = Modifier.padding(vertical = 10.dp),
                    color = Color(0xFFEEEEEE)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Total (${viewModel.totalItems} item)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = TextPrimary
                    )
                    Text(
                        text = viewModel.formatPrice(viewModel.totalPrice),
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = OrangePrimary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { /* Order action */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangePrimary)
                ) {
                    Text(
                        text = "Pesan Sekarang",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun MenuItemCard(
    item: FoodItem,
    onItemClick: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    formatPrice: (Double) -> String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(if (item.quantity > 0) 6.dp else 2.dp, RoundedCornerShape(16.dp))
            .clickable { onItemClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.quantity > 0) Color(0xFFFFF3EE) else Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Emoji icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (item.quantity > 0)
                            Brush.radialGradient(listOf(OrangeLight.copy(0.3f), OrangePrimary.copy(0.1f)))
                        else
                            Brush.radialGradient(listOf(Color(0xFFF5F5F5), Color(0xFFEEEEEE)))
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = item.emoji, fontSize = 32.sp)
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.category,
                    fontSize = 10.sp,
                    color = OrangePrimary,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 0.5.sp
                )
                Text(
                    text = item.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = TextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = formatPrice(item.price),
                    color = OrangeDark,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Quantity control
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (item.quantity > 0) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(24.dp))
                            .background(OrangePrimary)
                            .padding(horizontal = 2.dp, vertical = 2.dp)
                    ) {
                        IconButton(
                            onClick = onDecrease,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f))
                        ) {
                            Text(
                                text = "−",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = item.quantity.toString(),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            modifier = Modifier.padding(horizontal = 10.dp)
                        )
                        IconButton(
                            onClick = onIncrease,
                            modifier = Modifier
                                .size(30.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.25f))
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Add",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                } else {
                    IconButton(
                        onClick = onIncrease,
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(OrangePrimary)
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Active order indicator bar
        if (item.quantity > 0) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .background(
                        Brush.horizontalGradient(listOf(OrangePrimary, OrangeLight))
                    )
            )
        }
    }
}

@Composable
fun EmptyOrderState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "👆", fontSize = 28.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Tap + untuk menambahkan pesanan",
            color = TextHint,
            fontSize = 13.sp
        )
    }
}

@Composable
fun EmptyMenuState() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🍽️", fontSize = 64.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Menu tidak tersedia",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextPrimary
            )
        }
    }
}
