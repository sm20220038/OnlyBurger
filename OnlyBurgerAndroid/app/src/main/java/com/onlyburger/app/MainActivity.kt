package com.onlyburger.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.onlyburger.app.ui.MainScaffold
import com.onlyburger.app.ui.screens.LoginScreen
import com.onlyburger.app.ui.screens.RegisterScreen
import com.onlyburger.app.ui.theme.OnlyBurgerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnlyBurgerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AppRoot()
                }
            }
        }
    }
}

/**
 * Top-level navigation. Starts on the menu when a session already exists, otherwise on
 * the login screen. After authenticating, the back stack is cleared so Back does not
 * return to the auth screens.
 */
@Composable
private fun AppRoot() {
    val container = (LocalContext.current.applicationContext as OnlyBurgerApp).container
    val navController = rememberNavController()
    val start = if (container.authRepository.isLoggedIn) "main" else "login"

    NavHost(navController = navController, startDestination = start) {
        composable("login") {
            LoginScreen(
                onAuthenticated = {
                    navController.navigate("main") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") },
            )
        }
        composable("register") {
            RegisterScreen(
                onAuthenticated = {
                    navController.navigate("main") {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.popBackStack() },
            )
        }
        composable("main") {
            MainScaffold(
                onLoggedOut = {
                    navController.navigate("login") {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}
