package br.com.fiap.axoeduc.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import android.util.Patterns
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.axoeduc.R
import br.com.fiap.axoeduc.components.EmailInput
import br.com.fiap.axoeduc.components.SenhaInput

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var senha by remember { mutableStateOf("") }
    var enviado by remember { mutableStateOf(false) }

    fun isFormValid(): Boolean {
        return email.isNotBlank()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && senha.isNotBlank()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF4F67C6)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "EFUB",
                fontSize = 56.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFFFFFF),
                letterSpacing = 6.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            EmailInput(
                email = email,
                onValueChange = {
                    email = it
                    enviado = false
                },
                enviado = enviado
            )

            Spacer(modifier = Modifier.height(4.dp))

            SenhaInput(
                senha = senha,
                onValueChange = {
                    senha = it
                    enviado = false
                },
                enviado = enviado
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Esqueci minha senha",
                fontSize = 13.sp,
                color = Color(0xFFB0C4FF),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    enviado = true
                    if (isFormValid()) {
                        onLoginSuccess()
                    }
                },
                enabled = isFormValid(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3030D6),
                    disabledContainerColor = Color(0x663030D6),
                    disabledContentColor = Color(0x99FFFFFF)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
            ) {
                Text(
                    text = "Entrar",
                    fontSize = 18.sp,
                    color = Color(0xFFFFFFFF),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color(0x80FFFFFF)
                )
                Text(
                    text = "  ou  ",
                    fontSize = 14.sp,
                    color = Color(0xCCFFFFFF),
                    fontWeight = FontWeight.Medium
                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Color(0x80FFFFFF)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF)
                ),
                border = BorderStroke(1.dp, Color(0xAAFFFFFF))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = "Google",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Entrar com Google",
                    fontSize = 16.sp,
                    color = Color(0xFF606060),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.size(24.dp))
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(
                    Color(0x807995FF)
                )
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Não tem uma conta?",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFFFFFF),
            )
            TextButton(
                onClick = {},
                contentPadding = PaddingValues(start = 4.dp)
            ) {
                Text(
                    text = "Crie agora",
                    color = Color(0xFF0000FF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}