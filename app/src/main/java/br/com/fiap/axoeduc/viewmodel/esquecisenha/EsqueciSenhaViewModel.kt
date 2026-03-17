package br.com.fiap.axoeduc.viewmodel.esquecisenha

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class EsqueciSenhaViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var email by mutableStateOf("")
        private set
    var emailErro by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set
    var emailEnviado by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(novoValor: String) {
        email = novoValor
        emailErro = null
        errorMessage = null
        emailEnviado = false
    }

    fun onEmailFocusLost() {
        if (email.isNotBlank() && !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailErro = "E-mail inválido"
        }
    }

    fun isFormValid(): Boolean {
        return email.isNotBlank()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && emailErro == null
    }

    /**
     * Envia e-mail de redefinição de senha via Firebase Auth.
     * Por segurança, exibe mensagem de sucesso mesmo se o e-mail não existir.
     */
    fun enviarRedefinicao() {
        errorMessage = null
        emailEnviado = false

        emailErro = when {
            email.isBlank() -> "Campo obrigatório"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "E-mail inválido"
            else -> null
        }

        if (emailErro != null) return

        isLoading = true

        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { tarefa ->
                isLoading = false
                if (tarefa.isSuccessful) {
                    emailEnviado = true
                } else {
                    // Por segurança, mesmo com erro de "usuário não encontrado",
                    // exibimos sucesso para não revelar e-mails cadastrados.
                    // Só mostramos erro real em caso de falha de rede ou similar.
                    val exception = tarefa.exception
                    if (exception?.message?.contains("no user record", ignoreCase = true) == true
                        || exception?.message?.contains("INVALID_EMAIL", ignoreCase = true) == true
                    ) {
                        emailEnviado = true
                    } else {
                        errorMessage = "Erro ao enviar. Verifique sua conexão e tente novamente."
                    }
                }
            }
    }
}
