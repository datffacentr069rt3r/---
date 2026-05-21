package com.deafsmartglasses1.app.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deafsmartglasses1.app.ui.components.AmoledBlack
import com.deafsmartglasses1.app.ui.components.BorderSubtle
import com.deafsmartglasses1.app.ui.components.DangerPillButton
import com.deafsmartglasses1.app.ui.components.DangerRed
import com.deafsmartglasses1.app.ui.components.DarkPillButton
import com.deafsmartglasses1.app.ui.components.GlassesIcon
import com.deafsmartglasses1.app.ui.components.SuccessGreen
import com.deafsmartglasses1.app.ui.components.SurfaceDark
import com.deafsmartglasses1.app.ui.components.TextMuted
import com.deafsmartglasses1.app.ui.components.TextPrimary

@Composable
fun SettingsScreen(
    isConnected: Boolean,
    deviceName: String,
    onDisconnect: () -> Unit,
    onClearHistory: () -> Unit
) {
    val textSize = remember { mutableStateOf(1f) }
    val contrast = remember { mutableStateOf(1f) }
    val haptics = remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 22.dp)
            .padding(top = 62.dp, bottom = 118.dp)
    ) {
        Text(
            text = "Настройки",
            color = TextPrimary,
            fontSize = 34.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.SansSerif
        )
        Text(
            text = "Устройство, доступность и история",
            color = TextMuted,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(22.dp))

        SettingsCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background((if (isConnected) SuccessGreen else DangerRed).copy(alpha = 0.13f))
                        .padding(13.dp),
                    contentAlignment = Alignment.Center
                ) {
                    GlassesIcon(
                        modifier = Modifier.size(34.dp),
                        color = if (isConnected) SuccessGreen else DangerRed
                    )
                }
                Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                    Text(
                        text = if (isConnected) deviceName else "Очки не подключены",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = if (isConnected) "Устройство активно" else "Создайте пару на главном экране",
                        color = TextMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            if (isConnected) {
                Spacer(modifier = Modifier.height(16.dp))
                DangerPillButton(text = "Отключить устройство", onClick = onDisconnect)
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        SettingsCard {
            Text("Доступность", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
            SettingSlider(title = "Размер текста", value = textSize.value, onValueChange = { textSize.value = it })
            SettingSlider(title = "Контраст", value = contrast.value, onValueChange = { contrast.value = it })
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Тактильная отдача", color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("Вибрация при действиях", color = TextMuted, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
                Switch(checked = haptics.value, onCheckedChange = { haptics.value = it })
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        SettingsCard {
            Text("Данные", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Black)
            Text(
                text = "Очистка удалит локальную историю распознавания в текущей сессии.",
                color = TextMuted,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            DarkPillButton(text = "Очистить историю", onClick = onClearHistory)
        }
    }
}

@Composable
private fun SettingSlider(
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = TextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("${(value * 100).toInt()}%", color = TextMuted, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Slider(value = value, onValueChange = onValueChange, valueRange = 0.75f..1.35f)
    }
}

@Composable
private fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceDark, RoundedCornerShape(26.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(26.dp))
            .padding(20.dp),
        content = content
    )
}
