package com.deafsmartglasses1.app.history

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(onBackPressed: () -> Unit = {}) {
    var isSelectionMode by remember { mutableStateOf(false) }
    var isConfirmDeleteVisible by remember { mutableStateOf(false) }
    val selectedChats = remember { mutableStateListOf<String>() }

    val todayChats = listOf("Расспознание текста 14:32", "Расспознание текста 13:15", "Расспознание текста 12:07", "Расспознание текста 11:45")
    val yesterdayChats = listOf("Расспознание текста 20:40", "Расспознание текста 18:22", "Расспознание текста 15:10", "Расспознание текста 13:30")
    val sections = listOf(Section("Сегодня", todayChats), Section("Вчера", yesterdayChats))

    Scaffold(
        modifier = Modifier.background(Color.Black),
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text("История", color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Назад", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Black)
            )
        },
        bottomBar = { CustomBottomNavigationBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (isSelectionMode) {
                    TextButton(
                        onClick = {
                            isSelectionMode = false
                            selectedChats.clear()
                        }
                    ) {
                        Text("Отмена", color = Color.White)
                    }
                } else {
                    TextButton(
                        onClick = { isSelectionMode = true }
                    ) {
                        Text("Выбрать", color = Color.White)
                    }
                }
            }
            LazyColumn(modifier = Modifier.weight(1f)) {
                sections.forEach { section ->
                    item {
                        Text(
                            text = section.title,
                            color = Color.Gray,
                            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
                        )
                    }
                    items(section.items) { chat ->
                        ChatItem(
                            chat = chat,
                            isSelectionMode = isSelectionMode,
                            selectedChats = selectedChats,
                            onEnterSelectionMode = { isSelectionMode = true }
                        )
                    }
                }
            }
            if (isSelectionMode && selectedChats.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${selectedChats.size} чатов выбрано",
                        color = Color.White
                    )
                    IconButton(
                        onClick = { isConfirmDeleteVisible = true }
                    ) {
                        Icon(
                            Icons.Filled.Delete,
                            contentDescription = "Удалить",
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
    if (isConfirmDeleteVisible) {
        DeleteConfirmationDialog(
            onConfirm = {
                selectedChats.clear()
                isSelectionMode = false
                isConfirmDeleteVisible = false
            },
            onDismiss = { isConfirmDeleteVisible = false }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatItem(
    chat: String,
    isSelectionMode: Boolean,
    selectedChats: MutableList<String>,
    onEnterSelectionMode: () -> Unit
) {
    val isSelected = selectedChats.contains(chat)
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = tween(200),
        label = "scale"
    )
    val checkAlpha by animateFloatAsState(
        targetValue = if (isSelectionMode) 1f else 0f,
        animationSpec = tween(200),
        label = "checkAlpha"
    )

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (isSelectionMode) {
                        if (isSelected) selectedChats.remove(chat) else selectedChats.add(chat)
                    }
                },
                onLongClick = {
                    if (!isSelectionMode) {
                        onEnterSelectionMode()
                        selectedChats.add(chat)
                    }
                }
            )
            .scale(scale)
            .background(if (isSelected) Color.Gray else Color.Black, RoundedCornerShape(12.dp))
            .border(2.dp, Color.White, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ChatIcon(modifier = Modifier.size(40.dp), tint = Color.White)
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = chat, color = Color.White)
            if (isSelectionMode) {
                AnimatedVisibility(
                    visible = isSelectionMode,
                    enter = fadeIn(tween(300))
                ) {
                    Text(
                        text = if (isSelected) "Выбран" else "Нажмите для выбора",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
        if (isSelectionMode) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .graphicsLayer(alpha = checkAlpha, scaleX = checkAlpha, scaleY = checkAlpha)
            ) {
                CheckboxIcon(
                    checked = isSelected,
                    modifier = Modifier.fillMaxSize(),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E1E1E),
        title = { Text("Удалить чаты?", color = Color.White, style = MaterialTheme.typography.titleMedium) },
        text = { Text("Вы уверены, что хотите удалить выбранные чаты?", color = Color.White) },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Удалить", color = Color.White)
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Отмена", color = Color.White)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomNavigationBar() {
    NavigationBar(
        containerColor = Color.Black,
        contentColor = Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
            label = null
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { LiveIcon(modifier = Modifier.size(24.dp), tint = Color.White) },
            label = null
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { AiIcon(modifier = Modifier.size(24.dp), tint = Color.White) },
            label = null
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "History") },
            label = null
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Filled.MoreVert, contentDescription = "More") },
            label = null
        )
    }
}

@Composable
fun ChatIcon(modifier: Modifier, tint: Color) {
    androidx.compose.foundation.Canvas(modifier) {
        val stroke = size.width * 0.08f
        val path = Path().apply {
            moveTo(size.width * 0.2f, size.height * 0.3f)
            lineTo(size.width * 0.8f, size.height * 0.3f)
            lineTo(size.width * 0.8f, size.height * 0.7f)
            lineTo(size.width * 0.4f, size.height * 0.7f)
            lineTo(size.width * 0.25f, size.height * 0.85f)
            lineTo(size.width * 0.3f, size.height * 0.7f)
            lineTo(size.width * 0.2f, size.height * 0.7f)
            close()
        }
        drawPath(path, tint, style = Stroke(stroke, cap = StrokeCap.Round))
    }
}

@Composable
fun CheckboxIcon(checked: Boolean, modifier: Modifier, tint: Color) {
    androidx.compose.foundation.Canvas(modifier) {
        val stroke = size.width * 0.1f
        drawRect(
            color = tint,
            topLeft = Offset(size.width * 0.1f, size.height * 0.1f),
            size = Size(size.width * 0.8f, size.height * 0.8f),
            style = Stroke(stroke)
        )
        if (checked) {
            val path = Path().apply {
                moveTo(size.width * 0.3f, size.height * 0.5f)
                lineTo(size.width * 0.45f, size.height * 0.65f)
                lineTo(size.width * 0.7f, size.height * 0.35f)
            }
            drawPath(path, tint, style = Stroke(stroke, cap = StrokeCap.Round))
        }
    }
}

@Composable
fun LiveIcon(modifier: Modifier, tint: Color) {
    androidx.compose.foundation.Canvas(modifier) {
        val path = Path().apply {
            moveTo(size.width * 0.3f, size.height * 0.2f)
            lineTo(size.width * 0.3f, size.height * 0.8f)
            lineTo(size.width * 0.8f, size.height * 0.5f)
            close()
        }
        drawPath(path, tint)
    }
}

@Composable
fun AiIcon(modifier: Modifier, tint: Color) {
    androidx.compose.foundation.Canvas(modifier) {
        val stroke = size.width * 0.1f
        val cx = size.width / 2
        val cy = size.height / 2
        val r = size.width * 0.3f
        for (i in 0..3) {
            val angle = Math.toRadians(i * 90.0).toFloat()
            val x = cx + r * kotlin.math.cos(angle)
            val y = cy + r * kotlin.math.sin(angle)
            drawLine(tint, Offset(cx, cy), Offset(x, y), stroke, cap = StrokeCap.Round)
        }
        drawCircle(tint, radius = r * 0.2f, center = Offset(cx, cy))
    }
}

private data class Section(val title: String, val items: List<String>)

@Preview(showBackground = true)
@Composable
fun PreviewHistoryScreen() {
    HistoryScreen()
}