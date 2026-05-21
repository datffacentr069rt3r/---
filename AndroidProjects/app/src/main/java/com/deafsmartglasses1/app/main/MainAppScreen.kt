package com.deafsmartglasses1.app.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.deafsmartglasses1.app.ai.AiChatScreen
import com.deafsmartglasses1.app.history.HistoryScreen
import com.deafsmartglasses1.app.home.HomeScreen
import kotlinx.coroutines.delay

enum class InnerScreen {
    Home,
    History,
    Ai
}

@Composable
fun MainAppScreen() {
    var currentScreen by remember { mutableStateOf(InnerScreen.Home) }

    when (currentScreen) {
        InnerScreen.Home -> {
            HomeScreen(
                onHistoryClick = { currentScreen = InnerScreen.History },
                onOpenAi = { currentScreen = InnerScreen.Ai },
                onLiveClick = {},
                onMenuClick = {},
                onAddClick = {},
                onCreatePairClick = {},
                onInfoClick = {},
                onMoreClick = {}
            )
        }
        InnerScreen.History -> {
            HistoryScreen(
                onBackPressed = { currentScreen = InnerScreen.Home }
            )
        }
        InnerScreen.Ai -> {
            AiChatScreen(
                sendMessage = { userMessage ->
                    // Временно заглушка, замените на реальный AI
                    delay(1000)
                    "Вы сказали: $userMessage"
                },
                onBack = { currentScreen = InnerScreen.Home }
            )
        }
    }
}