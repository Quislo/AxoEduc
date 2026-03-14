package br.com.fiap.axoeduc.screens.cadastro

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import br.com.fiap.axoeduc.components.dialogs.DialogoPoliticaPrivacidade
import br.com.fiap.axoeduc.components.dialogs.DialogoRendaAlta
import br.com.fiap.axoeduc.components.dialogs.DialogoTermosUso
import br.com.fiap.axoeduc.components.IndicadorProgresso
import br.com.fiap.axoeduc.viewmodel.cadastro.CadastroViewModel

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember

@Composable
fun CadastroScreen(
    onCadastroSucesso: () -> Unit = {},
    onVoltarLogin: () -> Unit = {},
    viewModel: CadastroViewModel = viewModel()
) {

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel.cadastroRealizado) {
        if (viewModel.cadastroRealizado) {
            onCadastroSucesso()
        }
    }

    LaunchedEffect(viewModel.errorMessage) {
        viewModel.errorMessage?.let { msg ->
            snackbarHostState.showSnackbar(message = msg)
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
            // Título do app
            Text(
                text = "EFUB",
                fontSize = 40.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFFFFFFFF),
                letterSpacing = 4.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Indicador de progresso
            IndicadorProgresso(
                etapaAtual = viewModel.etapaAtual,
                totalEtapas = viewModel.totalEtapas
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título e subtítulo da etapa
            AnimatedContent(
                targetState = viewModel.etapaAtual,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { fullWidth -> viewModel.direcao * fullWidth }) togetherWith
                            slideOutHorizontally(targetOffsetX = { fullWidth -> -viewModel.direcao * fullWidth })
                },
                label = "titulo_etapa"
            ) { etapa ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = viewModel.titulosEtapas[etapa],
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFFFFFFF),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = viewModel.subtitulosEtapas[etapa],
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xCCFFFFFF),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Conteúdo dos campos por etapa
            AnimatedContent(
                targetState = viewModel.etapaAtual,
                transitionSpec = {
                    slideInHorizontally(initialOffsetX = { fullWidth -> viewModel.direcao * fullWidth }) togetherWith
                            slideOutHorizontally(targetOffsetX = { fullWidth -> -viewModel.direcao * fullWidth })
                },
                label = "campos_etapa"
            ) { etapa ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    when (etapa) {
                        0 -> {
                            EtapaDadosPessoais(
                                nome = viewModel.nome,
                                dataNascimento = viewModel.dataNascimento,
                                onNomeChange = {
                                    viewModel.nome = it
                                    viewModel.enviado = false
                                },
                                onDataChange = {
                                    viewModel.dataNascimento = it
                                    viewModel.enviado = false
                                },
                                enviado = viewModel.enviado
                            )
                        }

                        1 -> {
                            EtapaPerfilFinanceiro(
                                rendaMensal = viewModel.rendaMensal,
                                onRendaChange = {
                                    viewModel.rendaMensal = it
                                    viewModel.enviado = false
                                },
                                enviado = viewModel.enviado
                            )
                        }

                        2 -> {
                            EtapaDadosAcesso(
                                email = viewModel.email,
                                senha = viewModel.senha,
                                confirmarSenha = viewModel.confirmarSenha,
                                consentimentoOpenFinance = viewModel.consentimentoOpenFinance,
                                consentimentoLgpd = viewModel.consentimentoLgpd,
                                onEmailChange = { viewModel.onEmailChange(it) },
                                onSenhaChange = { viewModel.onSenhaChange(it) },
                                onConfirmarSenhaChange = { viewModel.onConfirmarSenhaChange(it) },
                                onConsentimentoOpenFinanceChange = { viewModel.consentimentoOpenFinance = it },
                                onConsentimentoLgpdChange = { viewModel.consentimentoLgpd = it },
                                onMostrarPolitica = { viewModel.mostrarPoliticaPrivacidade = true },
                                onMostrarTermos = { viewModel.mostrarTermosUso = true },
                                emailErro = viewModel.emailErro,
                                senhaErro = viewModel.senhaErro,
                                confirmarSenhaErro = viewModel.confirmarSenhaErro,
                                onEmailFocusLost = { viewModel.onEmailFocusLost() },
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Botões de navegação
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Botão Voltar (só aparece a partir da etapa 1)
                if (viewModel.etapaAtual > 0) {
                    Button(
                        onClick = { viewModel.voltarEtapa() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0x33FFFFFF)
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Voltar",
                            fontSize = 16.sp,
                            color = Color(0xFFFFFFFF),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Botão Próximo / Criar conta
                Button(
                    onClick = {
                        viewModel.avancarEtapa()
                    },
                    enabled = viewModel.isEtapaValida() && !viewModel.isLoading,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3030D6),
                        disabledContainerColor = Color(0x663030D6),
                        disabledContentColor = Color(0x99FFFFFF)
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    if (viewModel.isLoading && viewModel.etapaAtual == viewModel.totalEtapas - 1) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text(
                            text = if (viewModel.etapaAtual < viewModel.totalEtapas - 1) "Próximo" else "Criar conta",
                            fontSize = 16.sp,
                            color = Color(0xFFFFFFFF),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Indicador de etapa textual
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Etapa ${viewModel.etapaAtual + 1} de ${viewModel.totalEtapas}",
                fontSize = 13.sp,
                color = Color(0x99FFFFFF),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Rodapé — Já tem conta?
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
                text = "Já tem uma conta?",
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

        DialogoRendaAlta(
            mostrar = viewModel.mostrarDialogoRenda,
            onConfirmar = { viewModel.confirmarDialogoRenda() }
        )

        DialogoPoliticaPrivacidade(
            mostrar = viewModel.mostrarPoliticaPrivacidade,
            onFechar = { viewModel.mostrarPoliticaPrivacidade = false }
        )

        DialogoTermosUso(
            mostrar = viewModel.mostrarTermosUso,
            onFechar = { viewModel.mostrarTermosUso = false }
        )

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 16.dp)
        )

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CadastroScreenPreview() {
    CadastroScreen()
}