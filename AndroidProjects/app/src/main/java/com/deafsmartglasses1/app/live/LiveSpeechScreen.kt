package com.deafsmartglasses1.app.live

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deafsmartglasses1.app.ui.components.AmoledBlack
import com.deafsmartglasses1.app.ui.components.BorderSubtle
import com.deafsmartglasses1.app.ui.components.DangerRed
import com.deafsmartglasses1.app.ui.components.GlassesIcon
import com.deafsmartglasses1.app.ui.components.PremiumCard
import com.deafsmartglasses1.app.ui.components.SuccessGreen
import com.deafsmartglasses1.app.ui.components.SurfaceDark
import com.deafsmartglasses1.app.ui.components.TextMuted
import com.deafsmartglasses1.app.ui.components.TextPrimary
import com.deafsmartglasses1.app.ui.components.WhitePillButton
import com.deafsmartglasses1.app.ui.components.DarkPillButton
import kotlinx.coroutines.delay

@Composable
fun LiveSpeechScreen(
    isConnected: Boolean,
    onNewSpeech: (String) -> Unit
) {
    val isRunning = remember { mutableStateOf(false) }
    val recognizedItems = remember { mutableStateListOf<String>() }

    val mockPhrases = remember {
        listOf(
            "Здравствуйте, как вы себя чувствуете?",
            "Поверните направо, кабинет будет рядом с лифтом.",
            "Ваш документ готов, можно подойти к стойке регистрации.",
            "Занятие начнётся через пять минут.",
            "Оплата прошла успешно, чек отправлен на телефон.",
            "Следующая остановка через две минуты.",
            "Пожалуйста, повторите последнюю фразу."
        )
    }

    LaunchedEffect(isRunning.value) {
        var index = 0
        while (isRunning.value) {
            delay(1500)
            val phrase = mockPhrases[index % mockPhrases.size]
            recognizedItems.add(0, phrase)
            onNewSpeech(phrase)
            index++
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AmoledBlack)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 22.dp,
                top = 62.dp,
                end = 22.dp,
                bottom = 130.dp
            ),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Live Speech",
                    color = TextPrimary,
                    fontSize = 34.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.SansSerif
                )
                Text(
                    text = "Живые субтитры с умных очков",
                    color = TextMuted,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            item {
                StatusCard(
                    isConnected = isConnected,
                    isRunning = isRunning.value
                )
            }

            if (recognizedItems.isEmpty()) {
                item {
                    EmptySpeechCard(isConnected = isConnected)
                }
            } else {
                items(recognizedItems.take(12)) { phrase ->
                    SpeechCard(text = phrase)
                }
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(horizontal = 22.dp)
                .padding(bottom = 90.dp)
        ) {
            if (isRunning.value) {
                DarkPillButton(
                    text = "Остановить распознавание",
                    onClick = { isRunning.value = false }
                )
            } else {
                WhitePillButton(
                    text = if (isConnected) "Начать распознавание" else "Сначала подключите очки",
                    enabled = isConnected,
                    onClick = { if (isConnected) isRunning.value = true }
                )
            }
        }
    }
}

@Composable
private fun StatusCard(isConnected: Boolean, isRunning: Boolean) {
    PremiumCard(modifier = Modifier.fillMaxWidth()) {
        androidx.compose.foundation.layout.Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .clip(CircleShape)
                    .background((if (isConnected) SuccessGreen else DangerRed).copy(alpha = 0.13f))
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                GlassesIcon(
                    modifier = Modifier.size(34.dp),
                    color = if (isConnected) SuccessGreen else DangerRed
                )
            }
            Column(modifier = Modifier.padding(start = 14.dp).weight(1f)) {
                Text(
                    text = when {
                        !isConnected -> "Очки не подключены"
                        isRunning -> "Идёт распознавание"
                        else -> "Готово к запуску"
                    },
                    color = if (isConnected) SuccessGreen else DangerRed,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = if (isConnected) "Речь будет появляться ниже." else "Вернитесь на главный экран и создайте пару.",
                    color = TextMuted,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun EmptySpeechCard(isConnected: Boolean) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceDark, RoundedCornerShape(26.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(26.dp))
            .padding(22.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isConnected) "Нажмите старт" else "Нет подключения",
            color = TextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )
        Text(
            text = if (isConnected) "После запуска здесь появятся распознанные фразы." else "Подключите Deaf Smart Glasses на главном экране.",
            color = TextMuted,
            fontSize = 15.sp,
            lineHeight = 21.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun SpeechCard(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceDark, RoundedCornerShape(24.dp))
            .border(1.dp, BorderSubtle, RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        Text(
            text = text,
            color = TextPrimary,
            fontSize = 18.sp,
            lineHeight = 25.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Источник: Deaf Smart Glasses",
            color = TextMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
