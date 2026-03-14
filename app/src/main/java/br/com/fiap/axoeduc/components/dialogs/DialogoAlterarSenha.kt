package br.com.fiap.axoeduc.components.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DialogoAlterarSenha(
    senhaAtual: String,
    novaSenha: String,
    confirmarNovaSenha: String,
    onSenhaAtualChange: (String) -> Unit,
    onNovaSenhaChange: (String) -> Unit,
    onConfirmarNovaSenhaChange: (String) -> Unit,
    senhaAtualErro: String?,
    novaSenhaErro: String?,
    confirmarNovaSenhaErro: String?,
    isLoading: Boolean,
    onSalvar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = { if (!isLoading) onCancelar() },
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Text(
                text = "Alterar Senha",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.DarkGray
            )
        },
        text = {
            Column {
                CampoSenhaDialog(
                    valor = senhaAtual,
                    onValueChange = onSenhaAtualChange,
                    label = "Senha atual",
                    erro = senhaAtualErro,
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(12.dp))

                CampoSenhaDialog(
                    valor = novaSenha,
                    onValueChange = onNovaSenhaChange,
                    label = "Nova senha",
                    erro = novaSenhaErro,
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(12.dp))

                CampoSenhaDialog(
                    valor = confirmarNovaSenha,
                    onValueChange = onConfirmarNovaSenhaChange,
                    label = "Confirmar nova senha",
                    erro = confirmarNovaSenhaErro,
                    enabled = !isLoading
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onSalvar,
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3B4CCA)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Salvar", fontWeight = FontWeight.SemiBold)
            }
        },
        dismissButton = {
            TextButton(
                onClick = onCancelar,
                enabled = !isLoading
            ) {
                Text(
                    "Cancelar",
                    color = Color.Gray,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

@Composable
private fun CampoSenhaDialog(
    valor: String,
    onValueChange: (String) -> Unit,
    label: String,
    erro: String?,
    enabled: Boolean
) {
    var visivel by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = valor,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(label) },
            singleLine = true,
            enabled = enabled,
            isError = erro != null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (visivel) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = { visivel = !visivel }) {
                    Icon(
                        imageVector = if (visivel) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (visivel) "Ocultar senha" else "Mostrar senha",
                        tint = if (erro != null) Color(0xFFD32F2F) else Color(0xFF7B7D93)
                    )
                }
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF3B4CCA),
                focusedLabelColor = Color(0xFF3B4CCA),
                errorBorderColor = Color(0xFFD32F2F),
                errorLabelColor = Color(0xFFD32F2F)
            )
        )

        if (erro != null) {
            Text(
                text = erro,
                color = Color(0xFFD32F2F),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp, top = 4.dp)
            )
        }
    }
}