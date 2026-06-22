package com.onlyburger.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.onlyburger.app.data.remote.dto.OrderDto
import com.onlyburger.app.ui.components.StatusBadge
import com.onlyburger.app.ui.viewmodel.OrdersViewModel
import com.onlyburger.app.ui.viewmodel.rememberAppViewModelFactory
import com.onlyburger.app.util.Money

@Composable
fun OrdersScreen() {
    val viewModel: OrdersViewModel = viewModel(factory = rememberAppViewModelFactory())
    val state by viewModel.state.collectAsState()

    // Reload every time this screen is shown (e.g. after placing an order).
    LaunchedEffect(Unit) { viewModel.load() }

    when {
        state.loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.orders.isEmpty() && state.error == null -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "You have not placed any orders yet.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                state.error?.let { error ->
                    item {
                        Text(error, color = MaterialTheme.colorScheme.error)
                    }
                }
                items(state.orders, key = { it.id }) { order ->
                    OrderCard(
                        order = order,
                        onPay = { viewModel.pay(order.id) },
                        onCancel = { viewModel.cancel(order.id) },
                    )
                }
            }
        }
    }
}

@Composable
private fun OrderCard(
    order: OrderDto,
    onPay: () -> Unit,
    onCancel: () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Order #${order.id}", style = MaterialTheme.typography.titleMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    StatusBadge(order.status)
                    StatusBadge(order.paymentStatus)
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            order.items.forEach { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "${item.quantity} x ${item.productName}",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Text(
                        text = Money.format(item.lineTotal),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp))

            Text(
                text = "Deliver to: ${order.deliveryLocation}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text("Total", style = MaterialTheme.typography.titleSmall)
                Text(
                    text = Money.format(order.totalPrice),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                )
            }

            val canPay = order.paymentStatus == "Unpaid" && order.status != "Rejected"
            val canCancel = order.status == "Pending"
            if (canPay || canCancel) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    if (canPay) {
                        Button(onClick = onPay) { Text("Pay now") }
                    }
                    if (canCancel) {
                        OutlinedButton(onClick = onCancel) { Text("Cancel") }
                    }
                }
            }
        }
    }
}
