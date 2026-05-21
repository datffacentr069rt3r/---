package com.deafsmartglasses1.app.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

val AmoledBlack = Color(0xFF000000)
val SurfaceDark = Color(0xFF101010)
val SurfaceRaised = Color(0xFF171719)
val SurfaceSelected = Color(0xFF2B2B2E)
val TextPrimary = Color(0xFFFFFFFF)
val TextMuted = Color(0xFF8A8A8E)
val TextDim = Color(0xFF5F5F63)
val BorderSubtle = Color.White.copy(alpha = 0.08f)
val SuccessGreen = Color(0xFF30D158)
val DangerRed = Color(0xFFFF453A)
val AppleOrange = Color(0xFFFF9F0A)

@Stable
data class PremiumBottomNavItem(
    val title: String,
    val icon: @Composable (Color) -> Unit
)

@Composable
fun PressableScale(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    scaleTo: Float = 0.975f,
    onLongPress: (() -> Unit)? = null,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val scale = remember { Animatable(1f) }

    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale.value
                scaleY = scale.value
            }
            .pointerInput(enabled) {
                detectTapGestures(
                    onLongPress = {
                        if (enabled) {
                            mediumTap(context)
                            onLongPress?.invoke()
                        }
                    },
                    onPress = {
                        if (enabled) {
                            scope.launch {
                                scale.animateTo(
                                    targetValue = scaleTo,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }

                            val released = tryAwaitRelease()

                            scope.launch {
                                scale.animateTo(
                                    targetValue = 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                            }

                            if (released) {
                                softTap(context)
                                onClick()
                            }
                        }
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
fun CircleIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    PressableScale(
        modifier = modifier
            .size(50.dp)
            .clip(CircleShape)
            .background(SurfaceRaised)
            .border(1.dp, BorderSubtle, CircleShape),
        onClick = onClick
    ) {
        content()
    }
}

@Composable
fun WhitePillButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    PressableScale(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(if (enabled) Color.White else Color.White.copy(alpha = 0.32f)),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun DarkPillButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    PressableScale(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (enabled) SurfaceRaised else SurfaceRaised.copy(alpha = 0.45f))
            .border(1.dp, BorderSubtle, RoundedCornerShape(22.dp)),
        enabled = enabled,
        onClick = onClick
    ) {
        Text(
            text = text,
            color = if (enabled) Color.White else TextMuted,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun DangerPillButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    PressableScale(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(DangerRed.copy(alpha = 0.14f)),
        onClick = onClick
    ) {
        Text(
            text = text,
            color = DangerRed,
            fontSize = 15.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(28.dp))
            .background(SurfaceDark)
            .border(1.dp, BorderSubtle, RoundedCornerShape(28.dp))
            .padding(20.dp),
        content = content
    )
}

@Composable
fun PremiumBottomNav(
    selectedIndex: Int,
    items: List<PremiumBottomNavItem>,
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit
) {
    val context = LocalContext.current
    if (items.isEmpty()) return

    BoxWithConstraints(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(horizontal = 14.dp)
            .padding(bottom = 12.dp)
            .height(66.dp)
            .clip(RoundedCornerShape(34.dp))
            .background(SurfaceRaised.copy(alpha = 0.96f))
            .border(1.dp, BorderSubtle, RoundedCornerShape(34.dp))
            .padding(5.dp)
    ) {
        val safeIndex = selectedIndex.coerceIn(0, items.lastIndex)
        val gap = 4.dp
        val itemWidth = (maxWidth - gap * (items.size - 1)) / items.size
        val targetOffset = (itemWidth + gap) * safeIndex

        val indicatorOffset = animateDpAsState(
            targetValue = targetOffset,
            animationSpec = tween(300),
            label = "premium_nav_indicator"
        )

        Box(
            modifier = Modifier
                .offset(x = indicatorOffset.value)
                .width(itemWidth)
                .fillMaxHeight()
                .clip(RoundedCornerShape(29.dp))
                .background(SurfaceSelected)
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(gap)
        ) {
            items.forEachIndexed { index, item ->
                BottomNavButton(
                    title = item.title,
                    selected = safeIndex == index,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        selectionTap(context)
                        onItemClick(index)
                    }
                ) {
                    item.icon(if (safeIndex == index) TextPrimary else TextMuted)
                }
            }
        }
    }
}

@Composable
private fun BottomNavButton(
    title: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    icon: @Composable () -> Unit
) {
    val color = if (selected) TextPrimary else TextMuted

    PressableScale(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(29.dp)),
        scaleTo = 0.98f,
        onClick = onClick
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            icon()
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                color = color,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                maxLines = 1
            )
        }
    }
}

@Composable
fun MenuIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.08f
        drawLine(color, Offset(size.width * 0.18f, size.height * 0.34f), Offset(size.width * 0.82f, size.height * 0.34f), stroke)
        drawLine(color, Offset(size.width * 0.18f, size.height * 0.50f), Offset(size.width * 0.70f, size.height * 0.50f), stroke)
        drawLine(color, Offset(size.width * 0.18f, size.height * 0.66f), Offset(size.width * 0.82f, size.height * 0.66f), stroke)
    }
}

@Composable
fun PlusIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.08f
        drawLine(color, Offset(size.width * 0.50f, size.height * 0.18f), Offset(size.width * 0.50f, size.height * 0.82f), stroke)
        drawLine(color, Offset(size.width * 0.18f, size.height * 0.50f), Offset(size.width * 0.82f, size.height * 0.50f), stroke)
    }
}

@Composable
fun CloseIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.08f
        drawLine(color, Offset(size.width * 0.24f, size.height * 0.24f), Offset(size.width * 0.76f, size.height * 0.76f), stroke)
        drawLine(color, Offset(size.width * 0.76f, size.height * 0.24f), Offset(size.width * 0.24f, size.height * 0.76f), stroke)
    }
}

@Composable
fun HomeIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.075f
        drawLine(color, Offset(size.width * 0.18f, size.height * 0.52f), Offset(size.width * 0.50f, size.height * 0.24f), strokeWidth)
        drawLine(color, Offset(size.width * 0.50f, size.height * 0.24f), Offset(size.width * 0.82f, size.height * 0.52f), strokeWidth)
        drawRoundRect(color, Offset(size.width * 0.28f, size.height * 0.48f), Size(size.width * 0.44f, size.height * 0.34f), CornerRadius(6f, 6f), style = Stroke(width = strokeWidth))
    }
}

@Composable
fun LiveIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.075f
        drawLine(color, Offset(size.width * 0.16f, size.height * 0.70f), Offset(size.width * 0.16f, size.height * 0.42f), stroke)
        drawLine(color, Offset(size.width * 0.34f, size.height * 0.78f), Offset(size.width * 0.34f, size.height * 0.26f), stroke)
        drawLine(color, Offset(size.width * 0.52f, size.height * 0.66f), Offset(size.width * 0.52f, size.height * 0.38f), stroke)
        drawLine(color, Offset(size.width * 0.70f, size.height * 0.80f), Offset(size.width * 0.70f, size.height * 0.20f), stroke)
        drawLine(color, Offset(size.width * 0.88f, size.height * 0.62f), Offset(size.width * 0.88f, size.height * 0.46f), stroke)
    }
}

@Composable
fun HistoryIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = size.minDimension * 0.075f)
        drawCircle(color = color, radius = size.minDimension * 0.34f, center = Offset(size.width * 0.52f, size.height * 0.52f), style = stroke)
        drawLine(color, Offset(size.width * 0.52f, size.height * 0.52f), Offset(size.width * 0.52f, size.height * 0.30f), stroke.width)
        drawLine(color, Offset(size.width * 0.52f, size.height * 0.52f), Offset(size.width * 0.68f, size.height * 0.62f), stroke.width)
        drawLine(color, Offset(size.width * 0.20f, size.height * 0.34f), Offset(size.width * 0.10f, size.height * 0.34f), stroke.width)
    }
}

@Composable
fun SettingsIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = size.minDimension * 0.07f)
        drawCircle(color = color, radius = size.minDimension * 0.30f, center = Offset(size.width * 0.50f, size.height * 0.50f), style = stroke)
        drawCircle(color = color, radius = size.minDimension * 0.10f, center = Offset(size.width * 0.50f, size.height * 0.50f), style = stroke)
        drawLine(color, Offset(size.width * 0.50f, size.height * 0.06f), Offset(size.width * 0.50f, size.height * 0.20f), stroke.width)
        drawLine(color, Offset(size.width * 0.50f, size.height * 0.80f), Offset(size.width * 0.50f, size.height * 0.94f), stroke.width)
        drawLine(color, Offset(size.width * 0.06f, size.height * 0.50f), Offset(size.width * 0.20f, size.height * 0.50f), stroke.width)
        drawLine(color, Offset(size.width * 0.80f, size.height * 0.50f), Offset(size.width * 0.94f, size.height * 0.50f), stroke.width)
    }
}

@Composable
fun AiSparkIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier) {
        val stroke = size.minDimension * 0.07f
        drawLine(color, Offset(size.width * 0.50f, size.height * 0.12f), Offset(size.width * 0.50f, size.height * 0.88f), stroke)
        drawLine(color, Offset(size.width * 0.12f, size.height * 0.50f), Offset(size.width * 0.88f, size.height * 0.50f), stroke)
        drawCircle(color, radius = size.minDimension * 0.09f, center = Offset(size.width * 0.50f, size.height * 0.50f))
    }
}

@Composable
fun MicIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(width = size.minDimension * 0.075f)
        drawRoundRect(color, Offset(size.width * 0.36f, size.height * 0.12f), Size(size.width * 0.28f, size.height * 0.48f), CornerRadius(18f, 18f), style = stroke)
        drawLine(color, Offset(size.width * 0.24f, size.height * 0.46f), Offset(size.width * 0.24f, size.height * 0.58f), stroke.width)
        drawLine(color, Offset(size.width * 0.76f, size.height * 0.46f), Offset(size.width * 0.76f, size.height * 0.58f), stroke.width)
        drawLine(color, Offset(size.width * 0.50f, size.height * 0.68f), Offset(size.width * 0.50f, size.height * 0.86f), stroke.width)
        drawLine(color, Offset(size.width * 0.34f, size.height * 0.86f), Offset(size.width * 0.66f, size.height * 0.86f), stroke.width)
    }
}

@Composable
fun GlassesIcon(modifier: Modifier = Modifier, color: Color = Color.White) {
    Canvas(modifier = modifier) {
        val strokeWidth = size.minDimension * 0.08f
        val stroke = Stroke(width = strokeWidth)
        val lensWidth = size.width * 0.34f
        val lensHeight = size.height * 0.30f
        val top = size.height * 0.34f
        drawRoundRect(color, Offset(size.width * 0.08f, top), Size(lensWidth, lensHeight), CornerRadius(14f, 14f), style = stroke)
        drawRoundRect(color, Offset(size.width * 0.58f, top), Size(lensWidth, lensHeight), CornerRadius(14f, 14f), style = stroke)
        drawLine(color, Offset(size.width * 0.42f, size.height * 0.49f), Offset(size.width * 0.58f, size.height * 0.49f), strokeWidth)
    }
}
