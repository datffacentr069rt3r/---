package com.deafsmartglasses1.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.deafsmartglasses1.app.auth.AuthScreen
import com.deafsmartglasses1.app.main.MainAppScreen
import com.deafsmartglasses1.app.welcome.WelcomeScreen

enum class RootScreen {
    Welcome,
    Login,
    Register,
    Main
}

@Composable
fun AppNavigation() {
    val currentScreen = remember { mutableStateOf(RootScreen.Welcome) }

    when (currentScreen.value) {
        RootScreen.Welcome -> {
            WelcomeScreen(
                onLoginClick = {
                    currentScreen.value = RootScreen.Login
                },
                onRegisterClick = {
                    currentScreen.value = RootScreen.Register
                }
            )
        }

        RootScreen.Login -> {
            AuthScreen(
                title = "Войти",
                buttonText = "Войти",
                showName = false,
                onBack = {
                    currentScreen.value = RootScreen.Welcome
                },
                onSuccess = {
                    currentScreen.value = RootScreen.Main
                }
            )
        }

        RootScreen.Register -> {
            AuthScreen(
                title = "Создать аккаунт",
                buttonText = "Зарегистрироваться",
                showName = true,
                onBack = {
                    currentScreen.value = RootScreen.Welcome
                },
                onSuccess = {
                    currentScreen.value = RootScreen.Main
                }
            )
        }

        RootScreen.Main -> {
            MainAppScreen()
        }
    }
}