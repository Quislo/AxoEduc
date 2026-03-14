package br.com.fiap.axoeduc.components.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SenhaInput(
    senha: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Senha",
    placeholder: String = "Digite sua senha",
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
) {
    var senhaVisivel by remember { mutableStateOf(false) }

    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = senha,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            placeholder = { Text(placeholder, color = Color(0x99FFFFFF)) },
            singleLine = true,
            enabled = enabled,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (senhaVisivel) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { senhaVisivel = !senhaVisivel }) {
                    Icon(
                        imageVector = if (senhaVisivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (senhaVisivel) "Ocultar senha" else "Mostrar senha",
                        tint = if (isError) Color(0xFFFF6B6B) else Color(0xCCFFFFFF)
                    )
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedContainerColor = Color(0x1AFFFFFF),
                focusedContainerColor = Color(0x33FFFFFF),
                unfocusedBorderColor = Color(0xAAFFFFFF),
                focusedBorderColor = Color(0xFF7995FF),
                cursorColor = Color.White,
                unfocusedLabelColor = Color(0xCCFFFFFF),
                focusedLabelColor = Color(0xFF7995FF),
                unfocusedTextColor = Color.White,
                focusedTextColor = Color.White,
                errorBorderColor = Color(0xFFFF6B6B),
                errorLabelColor = Color(0xFFFF6B6B),
                errorCursorColor = Color(0xFFFF6B6B),
                errorTextColor = Color.White,
                disabledContainerColor = Color(0x0DFFFFFF),
                disabledBorderColor = Color(0x55FFFFFF),
                disabledLabelColor = Color(0x55FFFFFF),
                disabledTextColor = Color(0x55FFFFFF),
            )
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color(0xFFFF6B6B),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}