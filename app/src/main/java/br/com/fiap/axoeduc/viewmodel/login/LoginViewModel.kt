package br.com.fiap.axoeduc.viewmodel.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.axoeduc.repository.UsuarioRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {

    var email by mutableStateOf("")
    var senha by mutableStateOf("")

    // Estados de erro por campo — controlados pelo ViewModel, consumidos pela View
    var emailErro by mutableStateOf<String?>(null)
        private set
    var senhaErro by mutableStateOf<String?>(null)
        private set

    // Estados Assíncronos (Banco de Dados)
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var loginRealizado by mutableStateOf(false)
        private set

    /** Chamado pelo callback `onValueChange` do EmailInput. */
    fun onEmailChange(novoValor: String) {
        email = novoValor
        emailErro = null   // limpa erro ao digitar
        errorMessage = null
    }

    /** Chamado pelo callback `onValueChange` do SenhaInput. */
    fun onSenhaChange(novoValor: String) {
        senha = novoValor
        senhaErro = null   // limpa erro ao digitar
        errorMessage = null
    }

    /** Chamado pelo `onFocusLost` do EmailInput — valida formato inline. */
    fun onEmailFocusLost() {
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailErro = "E-mail inválido"
        }
    }

    fun isFormValid(): Boolean {
        return email.isNotBlank()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && senha.isNotBlank()
                && emailErro == null
                && senhaErro == null
    }

    /**
     * Tenta realizar o login validando a existência do Usuário no banco e a checagem da senha.
     * Altera `loginRealizado` para true se a validação for correta.
     */
    fun tentarLogin() {
        errorMessage = null

        // Validação por campo
        emailErro = when {
            email.isBlank() -> "Campo obrigatório"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "E-mail inválido"
            else -> null
        }

        senhaErro = when {
            senha.isBlank() -> "Campo obrigatório"
            else -> null
        }

        if (emailErro != null || senhaErro != null) return

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                val usuarioBD = repository.buscarPorEmail(email.trim())

                // Regra de Segurança: Mensagem genérica se der nulo (não existe) ou se a senha for diferente.
                if (usuarioBD == null || usuarioBD.senha != senha) {
                    errorMessage = "E-mail ou senha incorretos."
                    return@launch
                }

                // Se chegou aqui, E-mail existe e a senha bate.
                loginRealizado = true

            } catch (e: Exception) {
                errorMessage = "Falha ao consultar banco de dados: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}