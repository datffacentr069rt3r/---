package com.deafsmartglasses1.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.deafsmartglasses1.app.navigation.AppNavigation
import com.deafsmartglasses1.app.ui.theme.DeafSmartGlassesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DeafSmartGlassesTheme {
                AppNavigation()
            }
        }
    }
}