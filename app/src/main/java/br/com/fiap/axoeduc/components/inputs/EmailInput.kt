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
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun EmailInput(
    email: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "E-mail",
    placeholder: String? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    enabled: Boolean = true,
    onFocusLost: (() -> Unit)? = null,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = email,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .then(
                    if (onFocusLost != null) {
                        Modifier.onFocusChanged { focusState ->
                            if (!focusState.isFocused) onFocusLost()
                        }
                    } else Modifier
                ),
            label = { Text(label) },
            placeholder = placeholder?.let { { Text(it, color = Color(0x99FFFFFF)) } },
            singleLine = true,
            enabled = enabled,
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
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