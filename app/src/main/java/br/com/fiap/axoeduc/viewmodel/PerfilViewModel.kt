package br.com.fiap.axoeduc.viewmodel

import android.net.Uri
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.axoeduc.repository.UsuarioRepository
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val repository: UsuarioRepository,
    private val usuarioId: Int
) : ViewModel() {

    var nome by mutableStateOf("")
    var email by mutableStateOf("")

    var nomeOriginal by mutableStateOf("")
        private set
    var emailOriginal by mutableStateOf("")
        private set

    var nomeErro by mutableStateOf<String?>(null)
        private set
    var emailErro by mutableStateOf<String?>(null)
        private set
    var mensagemSucesso by mutableStateOf<String?>(null)
        private set
    var mensagemErro by mutableStateOf<String?>(null)
        private set
    var isLoading by mutableStateOf(false)
        private set

    var fotoPerfilUri by mutableStateOf<String?>(null)
        private set

    var mostrarDialogoSenha by mutableStateOf(false)
        private set
    var senhaAtual by mutableStateOf("")
    var novaSenha by mutableStateOf("")
    var confirmarNovaSenha by mutableStateOf("")

    var senhaAtualErro by mutableStateOf<String?>(null)
        private set
    var novaSenhaErro by mutableStateOf<String?>(null)
        private set
    var confirmarNovaSenhaErro by mutableStateOf<String?>(null)
        private set

    var editandoNome by mutableStateOf(false)
        private set
    var editandoEmail by mutableStateOf(false)
        private set

    val nomeAlterado: Boolean get() = nome.trim() != nomeOriginal
    val emailAlterado: Boolean get() = email.trim() != emailOriginal

    fun habilitarEdicaoNome() {
        editandoNome = true
    }

    fun habilitarEdicaoEmail() {
        editandoEmail = true
    }

    fun cancelarEdicaoNome() {
        nome = nomeOriginal
        nomeErro = null
        editandoNome = false
    }

    fun cancelarEdicaoEmail() {
        email = emailOriginal
        emailErro = null
        editandoEmail = false
    }

    init {
        carregarUsuario()
    }

    private fun carregarUsuario() {
        viewModelScope.launch {
            repository.buscarPorId(usuarioId).collectLatest { usuario ->
                usuario?.let {
                    nome = it.nome
                    email = it.email
                    nomeOriginal = it.nome
                    emailOriginal = it.email
                    fotoPerfilUri = it.fotoPerfil
                }
            }
        }
    }

    fun onNomeChange(novoNome: String) {
        nome = novoNome
        nomeErro = null
        limparFeedback()
    }

    fun onEmailChange(novoEmail: String) {
        email = novoEmail
        emailErro = null
        limparFeedback()
    }

    fun onSenhaAtualChange(valor: String) {
        senhaAtual = valor
        senhaAtualErro = null
    }

    fun onNovaSenhaChange(valor: String) {
        novaSenha = valor
        novaSenhaErro = null
    }

    fun onConfirmarNovaSenhaChange(valor: String) {
        confirmarNovaSenha = valor
        confirmarNovaSenhaErro = null
    }

    fun salvarNome() {
        val nomeTrimmed = nome.trim()

        nomeErro = when {
            nomeTrimmed.isBlank() -> "Campo obrigatório"
            nomeTrimmed.length < 3 -> "Mínimo de 3 caracteres"
            else -> null
        }

        if (nomeErro != null) return

        viewModelScope.launch {
            try {
                isLoading = true
                repository.atualizarNome(usuarioId, nomeTrimmed)
                nomeOriginal = nomeTrimmed
                nome = nomeTrimmed
                editandoNome = false
                mensagemSucesso = "Nome atualizado com sucesso"
            } catch (e: Exception) {
                mensagemErro = "Erro ao atualizar nome: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun salvarEmail() {
        val emailTrimmed = email.trim()

        emailErro = when {
            emailTrimmed.isBlank() -> "Campo obrigatório"
            !Patterns.EMAIL_ADDRESS.matcher(emailTrimmed).matches() -> "E-mail inválido"
            else -> null
        }

        if (emailErro != null) return

        viewModelScope.launch {
            try {
                isLoading = true
                val resultado = repository.atualizarEmail(usuarioId, emailTrimmed)
                resultado.fold(
                    onSuccess = {
                        emailOriginal = emailTrimmed
                        email = emailTrimmed
                        editandoEmail = false
                        mensagemSucesso = "E-mail atualizado com sucesso"
                    },
                    onFailure = {
                        emailErro = it.message
                    }
                )
            } catch (e: Exception) {
                mensagemErro = "Erro ao atualizar e-mail: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun abrirDialogoSenha() {
        senhaAtual = ""
        novaSenha = ""
        confirmarNovaSenha = ""
        senhaAtualErro = null
        novaSenhaErro = null
        confirmarNovaSenhaErro = null
        mostrarDialogoSenha = true
    }

    fun fecharDialogoSenha() {
        mostrarDialogoSenha = false
    }

    fun salvarSenha() {
        senhaAtualErro = if (senhaAtual.isBlank()) "Campo obrigatório" else null

        novaSenhaErro = when {
            novaSenha.isBlank() -> "Campo obrigatório"
            novaSenha.length < 4 -> "Mínimo de 4 caracteres"
            novaSenha == senhaAtual -> "A nova senha deve ser diferente da atual"
            else -> null
        }

        confirmarNovaSenhaErro = when {
            confirmarNovaSenha.isBlank() -> "Campo obrigatório"
            confirmarNovaSenha != novaSenha -> "As senhas não coincidem"
            else -> null
        }

        if (senhaAtualErro != null || novaSenhaErro != null || confirmarNovaSenhaErro != null) return

        viewModelScope.launch {
            try {
                isLoading = true
                val resultado = repository.atualizarSenha(usuarioId, senhaAtual, novaSenha)
                resultado.fold(
                    onSuccess = {
                        mostrarDialogoSenha = false
                        mensagemSucesso = "Senha alterada com sucesso"
                    },
                    onFailure = {
                        senhaAtualErro = it.message
                    }
                )
            } catch (e: Exception) {
                mensagemErro = "Erro ao alterar senha: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun atualizarFotoPerfil(uri: Uri?) {
        val uriString = uri?.toString()
        fotoPerfilUri = uriString
        viewModelScope.launch {
            try {
                repository.atualizarFotoPerfil(usuarioId, uriString)
                mensagemSucesso = "Foto de perfil atualizada"
            } catch (e: Exception) {
                mensagemErro = "Erro ao salvar foto: ${e.message}"
            }
        }
    }

    fun limparFeedback() {
        mensagemSucesso = null
        mensagemErro = null
    }
}