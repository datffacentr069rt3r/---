package com.deafsmartglasses1.app.welcome

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    val phrases = listOf(
        "Давайте начнём\nколлективное\nобсуждение",
        "Речь становится\nтекстом\nмгновенно",
        "Deaf Smart\nGlasses\nрядом",
        "AI помогает\nобщаться\nпроще",
        "Слышать глазами\nтеперь\nвозможно"
    )

    val typedText = remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        var phraseIndex = 0

        while (true) {
            val phrase = phrases[phraseIndex]

            for (i in 1..phrase.length) {
                typedText.value = phrase.take(i)

                if (phrase[i - 1] != '\n' && i % 2 == 0) {
                    typingHaptic(context)
                }

                delay(42)
            }

            delay(1100)

            for (i in phrase.length downTo 0) {
                typedText.value = phrase.take(i)

                if (i > 0 && i % 3 == 0) {
                    deletingHaptic(context)
                }

                delay(28)
            }

            delay(260)
            phraseIndex = (phraseIndex + 1) % phrases.size
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HeroTypingText(
            text = typedText.value,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-34).dp)
                .padding(horizontal = 26.dp)
        )

        BottomAuthPanel(
            onRegisterClick = onRegisterClick,
            onLoginClick = onLoginClick,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun HeroTypingText(
    text: String,
    modifier: Modifier = Modifier
) {
    val heroText = buildAnnotatedString {
        append(text)

        withStyle(
            style = SpanStyle(
                color = Color.White,
                fontSize = 46.sp,
                fontWeight = FontWeight.Black,
                baselineShift = BaselineShift(-0.18f)
            )
        ) {
            append("●")
        }
    }

    Text(
        text = heroText,
        color = Color.White,
        textAlign = TextAlign.Center,
        fontSize = 31.sp,
        lineHeight = 35.sp,
        fontWeight = FontWeight.Black,
        fontFamily = FontFamily.SansSerif,
        style = TextStyle(
            letterSpacing = (-0.75).sp
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
private fun BottomAuthPanel(
    onRegisterClick: () -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFF1C1C1E),
                shape = RoundedCornerShape(
                    topStart = 30.dp,
                    topEnd = 30.dp
                )
            )
            .padding(
                start = 20.dp,
                end = 20.dp,
                top = 22.dp,
                bottom = 30.dp
            )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AppleLikePrimaryButton(
                text = "Зарегистрироваться",
                onClick = onRegisterClick
            )

            AppleLikeSecondaryButton(
                text = "Войти",
                onClick = onLoginClick
            )
        }
    }
}

@Composable
private fun AppleLikePrimaryButton(
    text: String,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(1f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale.value)
            .background(
                color = Color(0xFFF5F5F7),
                shape = RoundedCornerShape(14.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        coroutineScope {
                            launch {
                                scale.animateTo(
                                    targetValue = 0.975f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }

                            val released = tryAwaitRelease()

                            launch {
                                scale.animateTo(
                                    targetValue = 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }

                            if (released) {
                                onClick()
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (-0.2).sp
        )
    }
}

@Composable
private fun AppleLikeSecondaryButton(
    text: String,
    onClick: () -> Unit
) {
    val scale = remember { Animatable(1f) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale.value)
            .background(
                color = Color(0xFF2C2C2E),
                shape = RoundedCornerShape(14.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFF3A3A3C),
                shape = RoundedCornerShape(14.dp)
            )
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        coroutineScope {
                            launch {
                                scale.animateTo(
                                    targetValue = 0.975f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }

                            val released = tryAwaitRelease()

                            launch {
                                scale.animateTo(
                                    targetValue = 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }

                            if (released) {
                                onClick()
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontSize = 17.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.SansSerif,
            letterSpacing = (-0.2).sp
        )
    }
}

private fun typingHaptic(context: Context) {
    val vibrator = context.getSystemService(Vibrator::class.java)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(
                    0,
                    12,
                    18,
                    12,
                    18,
                    12,
                    20
                ),
                intArrayOf(
                    0,
                    130,
                    0,
                    115,
                    0,
                    100,
                    0
                ),
                -1
            )
        )
    } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(34)
    }
}

private fun deletingHaptic(context: Context) {
    val vibrator = context.getSystemService(Vibrator::class.java)

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        vibrator?.vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(
                    0,
                    9,
                    10,
                    8,
                    12
                ),
                intArrayOf(
                    0,
                    100,
                    0,
                    65,
                    0
                ),
                -1
            )
        )
    } else {
        @Suppress("DEPRECATION")
        vibrator?.vibrate(26)
    }
}