package br.com.fiap.axoeduc.components.inputs

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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import java.util.Locale

class MascaraMoedaVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val valorOriginal = text.text

        if (valorOriginal.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val centavos = valorOriginal.toLongOrNull() ?: 0L
        val valor = centavos / 100.0

        val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val textoFormatado = formatador.format(valor)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return textoFormatado.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return valorOriginal.length
            }
        }

        return TransformedText(AnnotatedString(textoFormatado), offsetMapping)
    }
}

@Composable
fun RendaMensalInput(
    rendaMensal: String,
    onValueChange: (String) -> Unit,
    enviado: Boolean
) {
    var perdeuFoco by remember { mutableStateOf(false) }

    val mensagemErro = when {
        (enviado || perdeuFoco) && rendaMensal.isBlank() -> "Campo obrigatório"
        (enviado || perdeuFoco) && rendaMensal.isNotBlank() && (rendaMensal.toLongOrNull()
            ?: 0L) <= 0 -> "Informe um valor válido"

        else -> null
    }

    val invalido = mensagemErro != null

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = rendaMensal,
            onValueChange = { novoValor ->
                val apenasDigitos = novoValor.filter { it.isDigit() }
                if (apenasDigitos.length <= 12) {
                    onValueChange(apenasDigitos)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && rendaMensal.isNotEmpty()) {
                        perdeuFoco = true
                    }
                },
            label = {
                Text("Renda mensal")
            },
            placeholder = {
                Text("R$ 0,00", color = Color(0x99FFFFFF))
            },
            singleLine = true,
            isError = invalido,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = MascaraMoedaVisualTransformation(),
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
