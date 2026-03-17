package br.com.fiap.axoeduc.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.axoeduc.R
import br.com.fiap.axoeduc.components.inputs.EmailInput
import br.com.fiap.axoeduc.viewmodel.esquecisenha.EsqueciSenhaViewModel

@Composable
fun EsqueciSenhaScreen(
    onVoltarLogin: () -> Unit = {},
    viewModel: EsqueciSenhaViewModel = viewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4F67C6))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Logo
            Image(
                painter = painterResource(id = R.drawable.lg_educ),
                contentDescription = "Logo AxoEduc",
                modifier = Modifier
                    .height(220.dp)
                    .width(220.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Título
            Text(
                text = "Redefinir senha",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Subtítulo
            Text(
                text = "Informe seu e-mail para receber o link de redefinição",
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color(0xCCFFFFFF),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Campo de e-mail
            EmailInput(
                email = viewModel.email,
                onValueChange = { viewModel.onEmailChange(it) },
                isError = viewModel.emailErro != null,
                errorMessage = viewModel.emailErro,
                onFocusLost = { viewModel.onEmailFocusLost() },
                enabled = !viewModel.isLoading && !viewModel.emailEnviado,
            )

            // Banner de sucesso
            AnimatedVisibility(
                visible = viewModel.emailEnviado,
                enter = fadeIn(tween(200)) + expandVertically(
                    animationSpec = tween(250),
                    expandFrom = Alignment.Top
                ),
                exit = fadeOut(tween(200)) + shrinkVertically(
                    animationSpec = tween(200),
                    shrinkTowards = Alignment.Top
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x2630D636))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircleOutline,
                        contentDescription = "Sucesso",
                        tint = Color(0xFF6BFF6B),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "E-mail de redefinição enviado! Verifique sua caixa de entrada.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFD0FFD0),
                        lineHeight = 18.sp
                    )
                }
            }

            // Banner de erro
            AnimatedVisibility(
                visible = viewModel.errorMessage != null,
                enter = fadeIn(tween(200)) + expandVertically(
                    animationSpec = tween(250),
                    expandFrom = Alignment.Top
                ),
                exit = fadeOut(tween(200)) + shrinkVertically(
                    animationSpec = tween(200),
                    shrinkTowards = Alignment.Top
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x26FF3B30))
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ErrorOutline,
                        contentDescription = "Erro",
                        tint = Color(0xFFFF6B6B),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = viewModel.errorMessage ?: "",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFFD0CE),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Botão Enviar
            Button(
                onClick = { viewModel.enviarRedefinicao() },
                enabled = viewModel.isFormValid() && !viewModel.isLoading && !viewModel.emailEnviado,
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
                if (viewModel.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Enviar",
                        fontSize = 18.sp,
                        color = Color(0xFFFFFFFF),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Rodapé — Lembrou sua senha?
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .background(Color(0x807995FF))
                .padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lembrou sua senha?",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFFFFFF),
            )
            TextButton(
                onClick = onVoltarLogin,
                contentPadding = PaddingValues(start = 4.dp)
            ) {
                Text(
                    text = "Faça login",
                    color = Color(0xFF0000FF),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
