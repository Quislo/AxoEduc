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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar

class MascaraDataVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val digitos = text.text
        val sb = StringBuilder()

        for (i in digitos.indices) {
            if (i == 2 || i == 4) {
                sb.append('/')
            }
            sb.append(digitos[i])
        }

        val textoFormatado = sb.toString()

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val mapeado = when {
                    offset <= 2 -> offset
                    offset <= 4 -> offset + 1
                    offset <= 8 -> offset + 2
                    else -> textoFormatado.length
                }
                return mapeado.coerceAtMost(textoFormatado.length)
            }

            override fun transformedToOriginal(offset: Int): Int {
                val mapeado = when {
                    offset <= 2 -> offset
                    offset <= 5 -> offset - 1
                    offset <= 10 -> offset - 2
                    else -> digitos.length
                }
                return mapeado.coerceAtMost(digitos.length)
            }
        }

        return TransformedText(AnnotatedString(textoFormatado), offsetMapping)
    }
}

fun validarData(data: String): String? {
    if (data.length != 8) return "Data incompleta (DD/MM/AAAA)"

    val dia = data.substring(0, 2).toIntOrNull() ?: return "Data inválida"
    val mes = data.substring(2, 4).toIntOrNull() ?: return "Data inválida"
    val ano = data.substring(4, 8).toIntOrNull() ?: return "Data inválida"

    if (mes !in 1..12) return "Mês inválido"
    if (dia !in 1..31) return "Dia inválido"
    if (ano < 1900 || ano > Calendar.getInstance().get(Calendar.YEAR)) return "Ano inválido"

    val diasNoMes = when (mes) {
        2 -> if (ano % 4 == 0 && (ano % 100 != 0 || ano % 400 == 0)) 29 else 28
        4, 6, 9, 11 -> 30
        else -> 31
    }
    if (dia > diasNoMes) return "Dia inválido para este mês"

    val hoje = Calendar.getInstance()
    val nascimento = Calendar.getInstance().apply {
        set(Calendar.YEAR, ano)
        set(Calendar.MONTH, mes - 1)
        set(Calendar.DAY_OF_MONTH, dia)
    }
    var idade = hoje.get(Calendar.YEAR) - nascimento.get(Calendar.YEAR)
    if (hoje.get(Calendar.DAY_OF_YEAR) < nascimento.get(Calendar.DAY_OF_YEAR)) {
        idade--
    }
    if (idade < 16) return "Você deve ter no mínimo 16 anos"

    return null
}

@Composable
fun DataNascimentoInput(
    dataNascimento: String,
    onValueChange: (String) -> Unit,
    enviado: Boolean
) {
    var perdeuFoco by remember { mutableStateOf(false) }

    val mensagemErro = when {
        (enviado || perdeuFoco) && dataNascimento.isBlank() -> "Campo obrigatório"
        (enviado || perdeuFoco) && dataNascimento.isNotBlank() -> validarData(dataNascimento)
        else -> null
    }

    val invalido = mensagemErro != null

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = dataNascimento,
            onValueChange = { novoValor ->
                val apenasDigitos = novoValor.filter { it.isDigit() }
                if (apenasDigitos.length <= 8) {
                    onValueChange(apenasDigitos)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && dataNascimento.isNotEmpty()) {
                        perdeuFoco = true
                    }
                },
            label = {
                Text("Data de nascimento")
            },
            placeholder = {
                Text("DD/MM/AAAA", color = Color(0x99FFFFFF))
            },
            singleLine = true,
            isError = invalido,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            visualTransformation = MascaraDataVisualTransformation(),
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
