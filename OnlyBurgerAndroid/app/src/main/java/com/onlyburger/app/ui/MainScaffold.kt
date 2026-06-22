package com.onlyburger.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.onlyburger.app.OnlyBurgerApp
import com.onlyburger.app.ui.screens.CartScreen
import com.onlyburger.app.ui.screens.MenuScreen
import com.onlyburger.app.ui.screens.OrdersScreen
import com.onlyburger.app.ui.viewmodel.CartViewModel
import com.onlyburger.app.ui.viewmodel.rememberAppViewModelFactory

private data class BottomTab(val route: String, val label: String, val icon: ImageVector)

private val tabs = listOf(
    BottomTab("menu", "Menu", Icons.Default.Restaurant),
    BottomTab("cart", "Cart", Icons.Default.ShoppingCart),
    BottomTab("orders", "Orders", Icons.AutoMirrored.Filled.ReceiptLong),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(onLoggedOut: () -> Unit) {
    val container = (LocalContext.current.applicationContext as OnlyBurgerApp).container
    val cartViewModel: CartViewModel = viewModel(factory = rememberAppViewModelFactory())
    val cartState by cartViewModel.state.collectAsState()

    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(cartState.message) {
        cartState.message?.let {
            snackbarHostState.showSnackbar(it)
            cartViewModel.consumeMessage()
        }
    }
    LaunchedEffect(cartState.error) {
        cartState.error?.let {
            snackbarHostState.showSnackbar(it)
            cartViewModel.clearError()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("OnlyBurger") },
                actions = {
                    IconButton(onClick = {
                        container.authRepository.logout()
                        cartViewModel.clearOnLogout()
                        onLoggedOut()
                    }) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Log out")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    titleContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                    actionIconContentColor = androidx.compose.material3.MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        bottomBar = {
            NavigationBar {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = backStackEntry?.destination?.route
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = {
                            navController.navigate(tab.route) {
                                popUpTo(navController.graph.startDestinationId) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            if (tab.route == "cart" && cartState.itemCount > 0) {
                                BadgedBox(badge = { Badge { Text(cartState.itemCount.toString()) } }) {
                                    Icon(tab.icon, contentDescription = tab.label)
                                }
                            } else {
                                Icon(tab.icon, contentDescription = tab.label)
                            }
                        },
                        label = { Text(tab.label) },
                    )
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "menu",
            modifier = Modifier.padding(innerPadding),
        ) {
            composable("menu") { MenuScreen(cartViewModel) }
            composable("cart") {
                CartScreen(
                    cartViewModel = cartViewModel,
                    onCheckedOut = {
                        navController.navigate("orders") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
            composable("orders") { OrdersScreen() }
        }
    }
}
