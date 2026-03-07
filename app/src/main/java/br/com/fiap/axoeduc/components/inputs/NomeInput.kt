package br.com.fiap.axoeduc.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NomeInput(
    nome: String,
    onValueChange: (String) -> Unit,
    enviado: Boolean
) {
    var perdeuFoco by remember { mutableStateOf(false) }

    val mensagemErro = when {
        (enviado || perdeuFoco) && nome.isBlank() -> "Campo obrigatório"
        (enviado || perdeuFoco) && nome.trim().length < 3 -> "Nome deve ter pelo menos 3 caracteres"
        else -> null
    }

    val invalido = mensagemErro != null

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = nome,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && nome.isNotEmpty()) {
                        perdeuFoco = true
                    }
                },
            label = {
                Text("Nome completo")
            },
            placeholder = {
                Text("Digite seu nome", color = Color(0x99FFFFFF))
            },
            singleLine = true,
            isError = invalido,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words
            ),
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
                errorCursorColor = Color(0xFFFF6B6B)
            )
        )

        if (invalido) {
            Text(
                text = mensagemErro,
                color = Color(0xFFFF6B6B),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}
