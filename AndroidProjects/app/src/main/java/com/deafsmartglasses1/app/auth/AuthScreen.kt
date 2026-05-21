package com.deafsmartglasses1.app.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AuthScreen(
    title: String,
    buttonText: String,
    showName: Boolean,
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    val name = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 24.dp)
    ) {
        TextButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 48.dp)
        ) {
            Text("Назад", color = Color.White.copy(alpha = 0.75f))
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White.copy(alpha = 0.08f),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (showName) {
                    PremiumInput(
                        value = name.value,
                        onValueChange = { name.value = it },
                        label = "Имя"
                    )
                }

                PremiumInput(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = "Email",
                    keyboardType = KeyboardType.Email
                )

                PremiumInput(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = "Пароль",
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )

                Button(
                    onClick = onSuccess,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Text(buttonText, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PremiumInput(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(label)
        },
        singleLine = true,
        visualTransformation = if (isPassword) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color.White.copy(alpha = 0.5f),
            unfocusedBorderColor = Color.White.copy(alpha = 0.18f),
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.White.copy(alpha = 0.5f),
            cursorColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    )
}