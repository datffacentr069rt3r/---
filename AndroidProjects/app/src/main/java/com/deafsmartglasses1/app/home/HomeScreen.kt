package com.deafsmartglasses1.app.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.deafsmartglasses1.app.R

private val AppBlack = Color(0xFF000000)
private val CardBlack = Color(0xFF050505)
private val BorderColor = Color.White.copy(alpha = 0.10f)
private val White = Color.White
private val Secondary = Color.White.copy(alpha = 0.72f)
private val Inactive = Color.White.copy(alpha = 0.70f)

/* =========================================================
   🔧 ЦЕНТР УПРАВЛЕНИЯ ГЛАВНЫМ ЭКРАНОМ
   Размеры оставлены как у тебя. Меняй только здесь.
   ========================================================= */

// ====== ОБЩАЯ СЕТКА ЭКРАНА ======
private val ScreenPadding = 20.dp          // ← общий отступ слева/справа. Меньше = все блоки шире
private val ScreenTopPadding = 12.dp       // ← отступ сверху после status bar
private val ScreenBottomPadding = 10.dp    // ← отступ снизу после navigation bar

private val CardRadius = 16.dp             // ← скругление карточек: hero / pair / info
private val NavRadius = 16.dp              // ← скругление нижней навигации
private val CardBorderWidth = 1.dp         // ← толщина рамок карточек

// ====== ВЕРХНИЕ КНОПКИ МЕНЮ / ПЛЮС ======
private val TopBarHeight = 44.dp           // ← высота строки верхних кнопок
private val TopButtonSize = 42.dp          // ← размер круглых кнопок
private val TopButtonIconSize = 20.dp      // ← размер иконок внутри кнопок
private val TopButtonBorderWidth = 1.4.dp  // ← толщина обводки кнопок

// ====== ГЛАВНАЯ ГОТОВАЯ PNG-КАРТОЧКА ======
// glasses_hero.png = готовая большая карточка целиком.
// Код НЕ рисует внутри неё текст, иконки, линии и рамку.
private val HeroHeight = 325.dp            // ← высота главной карточки
private val HeroImageScaleX = 1.1f        // ← только ширина картинки внутри блока. 1.04 = чуть шире
private val HeroImageScaleY = 1.2f        // ← только высота картинки внутри блока
private val HeroInnerPadding = 0.dp        // ← внутренний отступ картинки. 0 = в край блока
private val HeroImageContentScale = ContentScale.Fit // ← Fit = без обрезки, Crop = обрезает края, FillBounds = растягивает

// ====== БЛОК "СОЗДАЙТЕ ПАРУ" ======
private val PairHeight = 175.dp            // ← высота карточки "Создайте пару"
private val PairPaddingHorizontal = 18.dp  // ← внутренний отступ слева/справа
private val PairPaddingVertical = 13.dp    // ← внутренний отступ сверху/снизу

private val PairTitleSize = 14.sp          // ← размер текста "Создайте пару"
private val PairTitleLineHeight = 20.sp    // ← высота строки заголовка
private val PairTitleWeight = FontWeight.SemiBold
private val PairTitleBottomGap = 1.dp      // ← отступ между заголовком и текстом

private val PairGlassesOffsetY = (-9).dp    // ← сдвиг очков вверх. Больше минус = выше

private val PairBodyTextSize = 8.sp       // ← размер описания
private val PairBodyLineHeight = 14.sp   // ← высота строки описания
private val PairBodyWeight = FontWeight.Normal
private val PairTextToImageGap = 0.dp      // ← зазор между текстом и картинкой справа

private val PairGlassesWidth = 215.dp      // ← ширина маленьких очков
private val PairGlassesHeight = 100.dp     // ← высота маленьких очков
private val PairGlassesScaleX = 1.50f      // ← масштаб очков внутри контейнера по ширине
private val PairGlassesScaleY = 1.75f      // ← масштаб очков внутри контейнера по высоте
private val PairGlassesCorner = 10.dp      // ← скругление контейнера очков
private val PairGlassesOffsetX = 45.dp      // ← сдвиг очков вправо. Больше = ближе к правому краю

private val PairContentToButtonGap = 5.dp  // ← отступ от контента до кнопки
private val PairButtonHorizontalPadding = 4.dp // ← чем меньше, тем кнопка шире
private val PairButtonHeight = 40.dp       // ← высота кнопки "Создать пару"
private val PairButtonRadius = 12.dp       // ← скругление кнопки. Было CircleShape, теперь управляется
private val PairButtonTextSize = 12.5.sp   // ← размер текста кнопки
private val PairButtonLineHeight = 15.sp   // ← высота строки текста кнопки
private val PairButtonTextWeight = FontWeight.Bold

// ====== БЛОК "УЗНАЙТЕ БОЛЬШЕ" ======
private val InfoHeight = 150.dp             // ← высота инфо-карточки
private val InfoPaddingHorizontal = 18.dp  // ← внутренний отступ слева/справа
private val InfoIconCircleSize = 24.dp     // ← размер кружка с i
private val InfoIconSize = 15.dp           // ← размер самой i
private val InfoIconBorderWidth = 1.4.dp
private val InfoIconToTextGap = 14.dp      // ← отступ от иконки до текста
private val InfoTextSize = 10.sp         // ← размер текста инфо-блока
private val InfoTextLineHeight = 18.sp     // ← высота строки инфо-блока
private val InfoArrowSize = 16.dp          // ← размер стрелки справа

// ====== НИЖНЯЯ НАВИГАЦИЯ ======
private val NavHeight = 60.dp              // ← высота нижней навигации
private val NavPaddingHorizontal = 8.dp    // ← внутренний отступ nav слева/справа
private val NavPaddingVertical = 5.dp      // ← внутренний отступ nav сверху/снизу
private val NavItemRadius = 16.dp          // ← скругление зоны клика таба
private val NavIconSize = 22.dp            // ← размер иконок навигации
private val NavIconToTextGap = 4.dp        // ← расстояние между иконкой и текстом
private val NavTextSize = 10.7.sp          // ← размер текста навигации
private val NavTextLineHeight = 12.sp      // ← высота строки текста навигации

// ====== ВЕРТИКАЛЬНЫЕ ОТСТУПЫ МЕЖДУ БЛОКАМИ ======
private val GapTopToHero = 18.dp           // ← от верхних кнопок до hero
private val GapHeroToPair = 18.dp          // ← от hero до "Создайте пару"
private val GapPairToInfo = 14.dp          // ← от "Создайте пару" до инфо
private val GapInfoToNav = 12.dp           // ← от инфо до нижней навигации

@Composable
fun HomeScreen(
    onOpenAi: () -> Unit = {},
    onChromeHiddenChange: (Boolean) -> Unit = {},
    onMenuClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onCreatePairClick: () -> Unit = {},
    onInfoClick: () -> Unit = {},
    onLiveClick: () -> Unit = {},
    onHistoryClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBlack)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = ScreenPadding)
            .padding(top = ScreenTopPadding, bottom = ScreenBottomPadding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = NavHeight + GapInfoToNav)
        ) {
            TopBar(
                height = TopBarHeight,
                buttonSize = TopButtonSize,
                iconSize = TopButtonIconSize,
                onMenuClick = onMenuClick,
                onAddClick = onAddClick
            )

            Spacer(modifier = Modifier.height(GapTopToHero))

            HeroImageCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(HeroHeight)
            )

            Spacer(modifier = Modifier.height(GapHeroToPair))

            PairingCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PairHeight),
                onCreatePairClick = onCreatePairClick
            )

            Spacer(modifier = Modifier.height(GapPairToInfo))

            InfoCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(InfoHeight),
                onClick = onInfoClick
            )
        }

        BottomNavigationBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(NavHeight),
            onHomeClick = {},
            onLiveClick = onLiveClick,
            onAiClick = onOpenAi,
            onHistoryClick = onHistoryClick,
            onMoreClick = onMoreClick
        )
    }
}

@Composable
private fun TopBar(
    height: Dp,
    buttonSize: Dp,
    iconSize: Dp,
    onMenuClick: () -> Unit,
    onAddClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(height),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleTopButton(
            size = buttonSize,
            onClick = onMenuClick
        ) {
            MenuIcon(
                modifier = Modifier.size(iconSize),
                color = White
            )
        }

        CircleTopButton(
            size = buttonSize,
            onClick = onAddClick
        ) {
            PlusIcon(
                modifier = Modifier.size(iconSize),
                color = White
            )
        }
    }
}

@Composable
private fun CircleTopButton(
    size: Dp,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .border(
                width = TopButtonBorderWidth,
                color = White,
                shape = CircleShape
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Composable
private fun HeroImageCard(
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(CardRadius))
            .background(CardBlack),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.glasses_hero),
            contentDescription = "Deaf Smart hero card",
            modifier = Modifier
                .fillMaxSize()
                .padding(HeroInnerPadding)
                .graphicsLayer {
                    scaleX = HeroImageScaleX
                    scaleY = HeroImageScaleY
                },
            contentScale = HeroImageContentScale
        )
    }
}

@Composable
private fun PairingCard(
    modifier: Modifier,
    onCreatePairClick: () -> Unit
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(CardRadius))
            .background(CardBlack)
            .border(
                width = CardBorderWidth,
                color = BorderColor,
                shape = RoundedCornerShape(CardRadius)
            )
            .padding(horizontal = PairPaddingHorizontal, vertical = PairPaddingVertical)
    ) {
        Text(
            text = "Создайте пару",
            color = White,
            fontSize = PairTitleSize,
            lineHeight = PairTitleLineHeight,
            fontWeight = PairTitleWeight,
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(PairTitleBottomGap))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Подключите ваши Deaf Smart к телефону и начните пользоваться всеми функциями.",
                color = Secondary,
                fontSize = PairBodyTextSize,
                lineHeight = PairBodyLineHeight,
                fontWeight = PairBodyWeight,
                modifier = Modifier.weight(1f),
                maxLines = 3
            )

            Spacer(modifier = Modifier.width(PairTextToImageGap))

            Box(
                modifier = Modifier
                    .width(PairGlassesWidth)
                    .height(PairGlassesHeight)
                    .offset(x = PairGlassesOffsetX, y = PairGlassesOffsetY)  // сдвиг очков вверх
                    .clip(RoundedCornerShape(PairGlassesCorner)),
                contentAlignment = Alignment.CenterEnd
            ) {
                Image(
                    painter = painterResource(id = R.drawable.glasses_outline),
                    contentDescription = "Glasses outline",
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer {
                            scaleX = PairGlassesScaleX
                            scaleY = PairGlassesScaleY
                        },
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(PairContentToButtonGap))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = PairButtonHorizontalPadding)
                .height(PairButtonHeight)
                .clip(RoundedCornerShape(PairButtonRadius))
                .background(Color(0xFFF2F2F2))
                .clickable(onClick = onCreatePairClick),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Создать пару",
                color = Color.Black,
                fontSize = PairButtonTextSize,
                lineHeight = PairButtonLineHeight,
                fontWeight = PairButtonTextWeight,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(CardRadius))
            .background(CardBlack)
            .border(
                width = CardBorderWidth,
                color = BorderColor,
                shape = RoundedCornerShape(CardRadius)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = InfoPaddingHorizontal),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(InfoIconCircleSize)
                .clip(CircleShape)
                .border(
                    width = InfoIconBorderWidth,
                    color = White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            InfoIcon(
                modifier = Modifier.size(InfoIconSize),
                color = White
            )
        }

        Spacer(modifier = Modifier.width(InfoIconToTextGap))

        Text(
            text = "Узнайте больше о возможностях\nDeaf Smart",
            color = White,
            fontSize = InfoTextSize,
            lineHeight = InfoTextLineHeight,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.weight(1f),
            maxLines = 2
        )

        ChevronRightIcon(
            modifier = Modifier.size(InfoArrowSize),
            color = White.copy(alpha = 0.90f)
        )
    }
}

@Composable
private fun BottomNavigationBar(
    modifier: Modifier,
    onHomeClick: () -> Unit,
    onLiveClick: () -> Unit,
    onAiClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(NavRadius))
            .background(CardBlack)
            .border(
                width = CardBorderWidth,
                color = BorderColor,
                shape = RoundedCornerShape(NavRadius)
            )
            .padding(horizontal = NavPaddingHorizontal, vertical = NavPaddingVertical),
        verticalAlignment = Alignment.CenterVertically
    ) {
        NavItem(
            modifier = Modifier.weight(1f),
            active = true,
            label = "Дом",
            icon = {
                HomeFilledIcon(
                    modifier = Modifier.size(NavIconSize),
                    color = White
                )
            },
            onClick = onHomeClick
        )

        NavItem(
            modifier = Modifier.weight(1f),
            active = false,
            label = "Live",
            icon = {
                WaveIcon(
                    modifier = Modifier.size(NavIconSize),
                    color = Inactive
                )
            },
            onClick = onLiveClick
        )

        NavItem(
            modifier = Modifier.weight(1f),
            active = false,
            label = "AI",
            icon = {
                SparklesIcon(
                    modifier = Modifier.size(NavIconSize),
                    color = Inactive
                )
            },
            onClick = onAiClick
        )

        NavItem(
            modifier = Modifier.weight(1f),
            active = false,
            label = "История",
            icon = {
                ClockIcon(
                    modifier = Modifier.size(NavIconSize),
                    color = Inactive
                )
            },
            onClick = onHistoryClick
        )

        NavItem(
            modifier = Modifier.weight(1f),
            active = false,
            label = "Еще",
            icon = {
                GridIcon(
                    modifier = Modifier.size(NavIconSize),
                    color = Inactive
                )
            },
            onClick = onMoreClick
        )
    }
}


@Composable
private fun NavItem(
    modifier: Modifier,
    active: Boolean,
    label: String,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clip(RoundedCornerShape(NavItemRadius))
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        icon()

        Spacer(modifier = Modifier.height(NavIconToTextGap))

        Text(
            text = label,
            color = if (active) White else Inactive,
            fontSize = NavTextSize,
            lineHeight = NavTextLineHeight,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
    }
}

/* ---------------- ICONS ---------------- */

@Composable
private fun MenuIcon(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val stroke = 2.4.dp.toPx()
        drawLine(color, Offset(size.width * 0.2f, size.height * 0.3f), Offset(size.width * 0.8f, size.height * 0.3f), stroke, StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.2f, size.height * 0.5f), Offset(size.width * 0.8f, size.height * 0.5f), stroke, StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.2f, size.height * 0.7f), Offset(size.width * 0.8f, size.height * 0.7f), stroke, StrokeCap.Round)
    }
}

@Composable
private fun PlusIcon(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val stroke = 2.4.dp.toPx()
        drawLine(color, Offset(size.width * 0.5f, size.height * 0.16f), Offset(size.width * 0.5f, size.height * 0.84f), stroke, StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.16f, size.height * 0.5f), Offset(size.width * 0.84f, size.height * 0.5f), stroke, StrokeCap.Round)
    }
}

@Composable
private fun InfoIcon(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        drawCircle(color = color, radius = 1.7.dp.toPx(), center = Offset(size.width * 0.50f, size.height * 0.22f))
        drawLine(
            color = color,
            start = Offset(size.width * 0.50f, size.height * 0.40f),
            end = Offset(size.width * 0.50f, size.height * 0.78f),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun ChevronRightIcon(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(2.4.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val path = Path().apply {
            moveTo(size.width * 0.36f, size.height * 0.20f)
            lineTo(size.width * 0.66f, size.height * 0.50f)
            lineTo(size.width * 0.36f, size.height * 0.80f)
        }
        drawPath(path, color, style = stroke)
    }
}

@Composable
private fun HomeFilledIcon(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val path = Path().apply {
            moveTo(size.width * 0.14f, size.height * 0.46f)
            lineTo(size.width * 0.50f, size.height * 0.16f)
            lineTo(size.width * 0.86f, size.height * 0.46f)
            lineTo(size.width * 0.77f, size.height * 0.53f)
            lineTo(size.width * 0.77f, size.height * 0.84f)
            lineTo(size.width * 0.24f, size.height * 0.84f)
            lineTo(size.width * 0.24f, size.height * 0.53f)
            close()
        }
        drawPath(path, color)
        drawRoundRect(
            color = AppBlack,
            topLeft = Offset(size.width * 0.45f, size.height * 0.62f),
            size = Size(size.width * 0.13f, size.height * 0.22f),
            cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
        )
    }
}

@Composable
private fun WaveIcon(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val stroke = 2.2.dp.toPx()
        val xs = listOf(0.18f, 0.32f, 0.46f, 0.60f, 0.74f, 0.88f)
        val heights = listOf(0.24f, 0.48f, 0.72f, 0.58f, 0.40f, 0.24f)
        xs.forEachIndexed { index, x ->
            val height = heights[index]
            drawLine(
                color = color,
                start = Offset(size.width * x, size.height * (0.5f - height / 2f)),
                end = Offset(size.width * x, size.height * (0.5f + height / 2f)),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
private fun SparklesIcon(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)

        fun sparkle(cx: Float, cy: Float, radius: Float) {
            val path = Path().apply {
                moveTo(cx, cy - radius)
                cubicTo(cx + radius * 0.15f, cy - radius * 0.25f, cx + radius * 0.25f, cy - radius * 0.15f, cx + radius, cy)
                cubicTo(cx + radius * 0.25f, cy + radius * 0.15f, cx + radius * 0.15f, cy + radius * 0.25f, cx, cy + radius)
                cubicTo(cx - radius * 0.15f, cy + radius * 0.25f, cx - radius * 0.25f, cy + radius * 0.15f, cx - radius, cy)
                cubicTo(cx - radius * 0.25f, cy - radius * 0.15f, cx - radius * 0.15f, cy - radius * 0.25f, cx, cy - radius)
            }
            drawPath(path, color, style = stroke)
        }

        sparkle(size.width * 0.43f, size.height * 0.42f, size.width * 0.22f)
        sparkle(size.width * 0.70f, size.height * 0.68f, size.width * 0.13f)
        sparkle(size.width * 0.74f, size.height * 0.18f, size.width * 0.08f)
    }
}

@Composable
private fun ClockIcon(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        drawCircle(color = color, radius = size.minDimension * 0.38f, center = Offset(size.width * 0.50f, size.height * 0.50f), style = stroke)
        drawLine(color, Offset(size.width * 0.50f, size.height * 0.50f), Offset(size.width * 0.50f, size.height * 0.27f), stroke.width, StrokeCap.Round)
        drawLine(color, Offset(size.width * 0.50f, size.height * 0.50f), Offset(size.width * 0.68f, size.height * 0.59f), stroke.width, StrokeCap.Round)
    }
}



@Composable
private fun GridIcon(modifier: Modifier, color: Color) {
    Canvas(modifier = modifier) {
        val stroke = Stroke(2.2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
        val cell = size.width * 0.22f
        val gap = size.width * 0.16f
        val startX = size.width * 0.20f
        val startY = size.height * 0.20f

        repeat(2) { row ->
            repeat(2) { col ->
                drawRoundRect(
                    color = color,
                    topLeft = Offset(x = startX + col * (cell + gap), y = startY + row * (cell + gap)),
                    size = Size(cell, cell),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
                    style = stroke
                )
            }
        }
    }
}
