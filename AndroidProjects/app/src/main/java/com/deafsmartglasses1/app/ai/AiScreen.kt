//package com.deafsmartglasses1.app.ai
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.ArrowBack
//import androidx.compose.material.icons.filled.Send
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.launch
//
//data class Message(val text: String, val isUser: Boolean)
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AiChatScreen(
//    sendMessage: suspend (String) -> String,
//    onBack: () -> Unit
//) {
//    val messages = remember { mutableStateListOf<Message>() }
//    var inputText by remember { mutableStateOf("") }
//    val scrollState = rememberLazyListState()
//    val coroutineScope = rememberCoroutineScope()
//
//    val quickQuestions = listOf(
//        "Как подключить очки?",
//        "Основные функции",
//        "Как работает распознавание текста?",
//        "Нужна помощь с заказом?"
//    )
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color.Black)
//    ) {
//        // Верхняя панель
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            IconButton(onClick = onBack) {
//                Icon(
//                    imageVector = Icons.Default.ArrowBack,
//                    contentDescription = "Назад",
//                    tint = Color.White
//                )
//            }
//            Spacer(modifier = Modifier.width(8.dp))
//            Column {
//                Text(
//                    "ИИ помощник",
//                    color = Color.White,
//                    style = MaterialTheme.typography.titleMedium
//                )
//                Text(
//                    "Задайте любой вопрос",
//                    color = Color.Gray,
//                    style = MaterialTheme.typography.bodySmall
//                )
//            }
//        }
//
//        // Быстрые вопросы
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 16.dp),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            val rows = quickQuestions.chunked(2)
//            rows.forEach { row ->
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.SpaceEvenly
//                ) {
//                    row.forEach { q ->
//                        Button(
//                            onClick = {
//                                coroutineScope.launch {
//                                    messages.add(Message(q, isUser = true))
//                                    scrollState.animateScrollToItem(messages.size - 1)
//                                    val reply = sendMessage(q)
//                                    messages.add(Message(reply, isUser = false))
//                                    scrollState.animateScrollToItem(messages.size - 1)
//                                }
//                            },
//                            shape = RoundedCornerShape(12.dp),
//                            colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray)
//                        ) {
//                            Text(q, color = Color.White)
//                        }
//                    }
//                }
//                Spacer(modifier = Modifier.height(8.dp))
//            }
//        }
//
//        // Список сообщений
//        LazyColumn(
//            modifier = Modifier
//                .weight(1f)
//                .padding(horizontal = 8.dp),
//            state = scrollState,
//            verticalArrangement = Arrangement.spacedBy(4.dp)
//        ) {
//            items(messages) { msg ->
//                ChatBubble(msg)
//            }
//        }
//
//        // Поле ввода
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            TextField(
//                value = inputText,
//                onValueChange = { inputText = it },
//                placeholder = { Text("Введите сообщение...", color = Color.Gray) },
//                modifier = Modifier.weight(1f),
//                colors = TextFieldDefaults.textFieldColors( // <- правильно
//                    textColor = Color.White,
//                    containerColor = Color.DarkGray,
//                    cursorColor = Color.White,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent
//                )
//            )
//
//            IconButton(
//                onClick = {
//                    if (inputText.isNotBlank()) {
//                        coroutineScope.launch {
//                            val userMsg = inputText
//                            messages.add(Message(userMsg, isUser = true))
//                            scrollState.animateScrollToItem(messages.size - 1)
//                            inputText = ""
//                            val reply = sendMessage(userMsg)
//                            messages.add(Message(reply, isUser = false))
//                            scrollState.animateScrollToItem(messages.size - 1)
//                        }
//                    }
//                }
//            ) {
//                val iconVector = if (inputText.isBlank()) Icons.Default.Send else Icons.Default.Send
//                Icon(
//                    imageVector = iconVector,
//                    contentDescription = null,
//                    tint = Color.White
//                )
//            }
//        }
//    }
//}
//
//@Composable
//fun ChatBubble(message: Message) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
//    ) {
//        Box(
//            modifier = Modifier
//                .background(
//                    color = if (message.isUser) Color(0xFF1E88E5) else Color.Gray,
//                    shape = RoundedCornerShape(12.dp)
//                )
//                .padding(12.dp)
//        ) {
//            Text(message.text, color = Color.White)
//        }
//    }
//}