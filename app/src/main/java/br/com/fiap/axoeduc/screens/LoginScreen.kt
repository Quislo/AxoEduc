package br.com.fiap.axoeduc.screens

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.axoeduc.R
import br.com.fiap.axoeduc.components.inputs.EmailInput
import br.com.fiap.axoeduc.components.inputs.SenhaInput
import br.com.fiap.axoeduc.viewmodel.login.LoginViewModel
import br.com.fiap.axoeduc.BuildConfig
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit = {},
    onCriarConta: () -> Unit = {},
    onCadastroIncompleto: (usuarioId: Int) -> Unit = {},
    viewModel: LoginViewModel = viewModel()
) {
    // Controla visibilidade do banner de erro inline
    var showErrorBanner by remember { mutableStateOf(false) }

    // Animatable para o efeito de "shake" horizontal nos inputs
    val shakeOffset = remember { Animatable(0f) }

    // Google Sign-In
    val contexto = LocalContext.current
    val credentialManager = remember { CredentialManager.create(contexto) }
    val escopo = rememberCoroutineScope()

    // Dispara navegação ao login bem-sucedido
    LaunchedEffect(viewModel.loginRealizado) {
        if (viewModel.loginRealizado) {
            if (viewModel.cadastroIncompleto) {
                onCadastroIncompleto(viewModel.usuarioLogadoId ?: 0)
            } else {
                onLoginSuccess()
            }
        }
    }

    // Reage a erros: exibe banner + dispara shake + auto-dismiss após 4s
    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let {
            showErrorBanner = true

            // Shake: sequência de offsets para simular vibração lateral
            launch {
                val shakeValues = listOf(10f, -10f, 8f, -8f, 5f, -5f, 2f, 0f)
                for (value in shakeValues) {
                    shakeOffset.animateTo(
                        targetValue = value,
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessHigh
                        )
                    )
                }
            }

            // Auto-dismiss do banner após 4 segundos
            delay(4_000)
            showErrorBanner = false
        }
    }

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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset { IntOffset(shakeOffset.value.roundToInt(), 0) },
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                EmailInput(
                    email = viewModel.email,
                    onValueChange = {
                        viewModel.onEmailChange(it)
                        showErrorBanner = false
                    },
                    isError = viewModel.emailErro != null || showErrorBanner,
                    errorMessage = viewModel.emailErro,
                    onFocusLost = { viewModel.onEmailFocusLost() },
                    enabled = !viewModel.isLoading,
                )

                Spacer(modifier = Modifier.height(4.dp))

                SenhaInput(
                    senha = viewModel.senha,
                    onValueChange = {
                        viewModel.onSenhaChange(it)
                        showErrorBanner = false
                    },
                    isError = viewModel.senhaErro != null || showErrorBanner,
                    errorMessage = viewModel.senhaErro,
                    enabled = !viewModel.isLoading,
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Esqueci minha senha",
                fontSize = 13.sp,
                color = Color(0xFFB0C4FF),
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Start,
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(
                visible = showErrorBanner,
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
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x26FF3B30))   // vermelho translúcido
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
                        text = viewModel.errorMessage ?: "E-mail ou senha incorretos.",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFFFFD0CE),
                        lineHeight = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { viewModel.tentarLogin() },
                enabled = viewModel.isFormValid() && !viewModel.isLoading,
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
                    androidx.compose.material3.CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Entrar",
                        fontSize = 18.sp,
                        color = Color(0xFFFFFFFF),
                        fontWeight = FontWeight.Bold
                    )
                }
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
                onClick = {
                    escopo.launch {
                        try {
                            val googleIdOption = GetGoogleIdOption.Builder()
                                .setFilterByAuthorizedAccounts(false)
                                .setServerClientId(BuildConfig.GOOGLE_WEB_CLIENT_ID)
                                .build()

                            val request = GetCredentialRequest.Builder()
                                .addCredentialOption(googleIdOption)
                                .build()

                            val result = credentialManager.getCredential(
                                contexto as Activity,
                                request
                            )
                            val credential = result.credential

                            if (credential is CustomCredential &&
                                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                            ) {
                                val googleCredential =
                                    GoogleIdTokenCredential.createFrom(credential.data)
                                viewModel.loginComGoogle(
                                    nome = googleCredential.displayName ?: "",
                                    email = googleCredential.id,
                                    googleId = googleCredential.idToken,
                                    fotoPerfil = googleCredential.profilePictureUri?.toString()
                                )
                            }
                        } catch (e: androidx.credentials.exceptions.GetCredentialCancellationException) {
                            // Usuário cancelou — sem ação
                        } catch (e: Exception) {
                            android.util.Log.e("LoginScreen", "Google Sign-In falhou", e)
                            viewModel.errorMessage = "Erro no Google Sign-In: ${e.message}"
                        }
                    }
                },
                enabled = !viewModel.googleLoginEmProgresso && !viewModel.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFFFFF)
                ),
                border = BorderStroke(1.dp, Color(0xAAFFFFFF))
            ) {
                if (viewModel.googleLoginEmProgresso) {
                    androidx.compose.material3.CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color(0xFF606060),
                        strokeWidth = 2.dp
                    )
                } else {
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
        }

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
                text = "Não tem uma conta?",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFFFFFFF),
            )
            TextButton(
                onClick = onCriarConta,
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