package br.com.fiap.axoeduc.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.com.fiap.axoeduc.components.dialogs.DialogoAlterarSenha
import br.com.fiap.axoeduc.viewmodel.PerfilViewModel
import coil.compose.AsyncImage
import kotlinx.coroutines.delay

@Composable
fun PerfilScreen(
    onVoltarClick: () -> Unit = {},
    onReportarBugClick: () -> Unit = {},
    onFaleConoscoClick: () -> Unit = {},
    onSairClick: () -> Unit = {},
    viewModel: PerfilViewModel,
) {
    // Auto-dismiss das mensagens de feedback após 3 segundos
    LaunchedEffect(viewModel.mensagemSucesso) {
        if (viewModel.mensagemSucesso != null) {
            delay(3_000)
            viewModel.limparFeedback()
        }
    }

    LaunchedEffect(viewModel.mensagemErro) {
        if (viewModel.mensagemErro != null) {
            delay(4_000)
            viewModel.limparFeedback()
        }
    }

    // Photo Picker launcher
    val fotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { viewModel.atualizarFotoPerfil(it) }
    }

    // Dialog de Alterar Senha
    if (viewModel.mostrarDialogoSenha) {
        DialogoAlterarSenha(
            senhaAtual = viewModel.senhaAtual,
            novaSenha = viewModel.novaSenha,
            confirmarNovaSenha = viewModel.confirmarNovaSenha,
            onSenhaAtualChange = viewModel::onSenhaAtualChange,
            onNovaSenhaChange = viewModel::onNovaSenhaChange,
            onConfirmarNovaSenhaChange = viewModel::onConfirmarNovaSenhaChange,
            senhaAtualErro = viewModel.senhaAtualErro,
            novaSenhaErro = viewModel.novaSenhaErro,
            confirmarNovaSenhaErro = viewModel.confirmarNovaSenhaErro,
            isLoading = viewModel.isLoading,
            onSalvar = viewModel::salvarSenha,
            onCancelar = viewModel::fecharDialogoSenha
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // --- Header ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVoltarClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.DarkGray
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Perfil",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray,
                modifier = Modifier.padding(end = 48.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(
            modifier = Modifier.size(140.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .border(width = 4.dp, color = Color(0xFF2E2E2E), shape = CircleShape)
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {
                if (viewModel.fotoPerfilUri != null) {
                    AsyncImage(
                        model = viewModel.fotoPerfilUri,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.size(80.dp),
                        tint = Color(0xFF2E2E2E)
                    )
                }
            }

            IconButton(
                onClick = {
                    fotoPickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 12.dp, bottom = 12.dp)
                    .size(32.dp)
                    .background(Color.White, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar foto",
                    tint = Color.Black,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.nome,
            onValueChange = viewModel::onNomeChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Nome") },
            isError = viewModel.nomeErro != null,
            supportingText = if (viewModel.nomeErro != null) {
                { Text(viewModel.nomeErro!!, color = Color(0xFFD32F2F)) }
            } else null,
            enabled = viewModel.editandoNome && !viewModel.isLoading,
            readOnly = !viewModel.editandoNome,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF3B4CCA),
                focusedLabelColor = Color(0xFF3B4CCA),
                errorBorderColor = Color(0xFFD32F2F),
                errorLabelColor = Color(0xFFD32F2F),
                disabledContainerColor = Color(0xFFE0E0E0),
                disabledBorderColor = Color(0xFFBDBDBD),
                disabledTextColor = Color(0xFF424242),
                disabledLabelColor = Color(0xFF757575)
            ),
            trailingIcon = {
                if (viewModel.editandoNome) {
                    Row {
                        if (viewModel.nomeAlterado) {
                            IconButton(onClick = { viewModel.salvarNome() }) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Salvar Nome",
                                    tint = Color(0xFF4CAF50)
                                )
                            }
                        }
                        IconButton(onClick = { viewModel.cancelarEdicaoNome() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancelar edição",
                                tint = Color(0xFFD32F2F)
                            )
                        }
                    }
                } else {
                    IconButton(onClick = { viewModel.habilitarEdicaoNome() }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar Nome",
                            tint = Color(0xFF7B7D93)
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.email,
            onValueChange = viewModel::onEmailChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            isError = viewModel.emailErro != null,
            supportingText = if (viewModel.emailErro != null) {
                { Text(viewModel.emailErro!!, color = Color(0xFFD32F2F)) }
            } else null,
            enabled = viewModel.editandoEmail && !viewModel.isLoading,
            readOnly = !viewModel.editandoEmail,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF3B4CCA),
                focusedLabelColor = Color(0xFF3B4CCA),
                errorBorderColor = Color(0xFFD32F2F),
                errorLabelColor = Color(0xFFD32F2F),
                disabledContainerColor = Color(0xFFE0E0E0),
                disabledBorderColor = Color(0xFFBDBDBD),
                disabledTextColor = Color(0xFF424242),
                disabledLabelColor = Color(0xFF757575)
            ),
            trailingIcon = {
                if (viewModel.editandoEmail) {
                    Row {
                        if (viewModel.emailAlterado) {
                            IconButton(onClick = { viewModel.salvarEmail() }) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Salvar Email",
                                    tint = Color(0xFF4CAF50)
                                )
                            }
                        }
                        IconButton(onClick = { viewModel.cancelarEdicaoEmail() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Cancelar edição",
                                tint = Color(0xFFD32F2F)
                            )
                        }
                    }
                } else {
                    IconButton(onClick = { viewModel.habilitarEdicaoEmail() }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar E-mail",
                            tint = Color(0xFF7B7D93)
                        )
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = viewModel.mensagemSucesso != null,
            enter = fadeIn(tween(200)) + expandVertically(tween(200)),
            exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
        ) {
            Text(
                text = viewModel.mensagemSucesso ?: "",
                color = Color(0xFF4CAF50),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x1A4CAF50))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }

        AnimatedVisibility(
            visible = viewModel.mensagemErro != null,
            enter = fadeIn(tween(200)) + expandVertically(tween(200)),
            exit = fadeOut(tween(200)) + shrinkVertically(tween(200))
        ) {
            Text(
                text = viewModel.mensagemErro ?: "",
                color = Color(0xFFD32F2F),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0x1AD32F2F))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val blueButtonColor = Color(0xFF3B4CCA)

        ActionBtn(
            text = "Alterar senha",
            containerColor = blueButtonColor,
            onClick = { viewModel.abrirDialogoSenha() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        ActionBtn(
            text = "Reportar bug",
            containerColor = blueButtonColor,
            onClick = onReportarBugClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        ActionBtn(
            text = "Fale conosco",
            containerColor = blueButtonColor,
            onClick = onFaleConoscoClick
        )

        Spacer(modifier = Modifier.weight(1f))

        ActionBtn(
            text = "Sair",
            containerColor = Color(0xFFF4511E),
            onClick = onSairClick
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ActionBtn(
    text: String,
    containerColor: Color,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(26.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
    }
}