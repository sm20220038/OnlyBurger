package com.onlyburger.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.onlyburger.app.data.remote.dto.ProductDto
import com.onlyburger.app.ui.components.ProductImage
import com.onlyburger.app.ui.viewmodel.CartViewModel
import com.onlyburger.app.ui.viewmodel.MenuViewModel
import com.onlyburger.app.ui.viewmodel.rememberAppViewModelFactory
import com.onlyburger.app.util.Money
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MenuScreen(cartViewModel: CartViewModel) {
    val viewModel: MenuViewModel = viewModel(factory = rememberAppViewModelFactory())
    val state by viewModel.state.collectAsState()

    when {
        state.loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.error != null -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(state.error!!, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = { viewModel.load() }) { Text("Retry") }
            }
        }

        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(state.products, key = { it.id }) { product ->
                    ProductCard(
                        product = product,
                        onAdd = { quantity ->
                            cartViewModel.addToCart(product.id, product.name, quantity)
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun ProductCard(
    product: ProductDto,
    onAdd: (Int) -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            ProductImage(
                productId = product.id,
                name = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(product.name, style = MaterialTheme.typography.titleMedium)
                Text(
                    text = product.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = Money.format(product.price),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    AddToCartControls(onAdd = onAdd)
                }
            }
        }
    }
}

@Composable
private fun AddToCartControls(onAdd: (Int) -> Unit) {
    var quantity by remember { mutableIntStateOf(1) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = { if (quantity > 1) quantity-- }) {
            Icon(Icons.Default.Remove, contentDescription = "Decrease quantity")
        }
        Text(quantity.toString(), style = MaterialTheme.typography.titleMedium)
        IconButton(onClick = { if (quantity < 1000) quantity++ }) {
            Icon(Icons.Default.Add, contentDescription = "Increase quantity")
        }
        Button(
            onClick = {
                onAdd(quantity)
                quantity = 1
            },
            modifier = Modifier.padding(start = 8.dp),
        ) {
            Text("Add")
        }
    }
}
