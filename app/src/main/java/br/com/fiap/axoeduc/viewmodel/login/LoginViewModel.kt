package br.com.fiap.axoeduc.viewmodel.login

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.axoeduc.model.Usuario
import br.com.fiap.axoeduc.repository.UsuarioRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var email by mutableStateOf("")
    var senha by mutableStateOf("")

    var emailErro by mutableStateOf<String?>(null)
        private set
    var senhaErro by mutableStateOf<String?>(null)
        private set

    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
    var loginRealizado by mutableStateOf(false)
        private set
    var usuarioLogadoUid by mutableStateOf<String?>(null)
        private set

    var googleLoginEmProgresso by mutableStateOf(false)
        private set
    var cadastroIncompleto by mutableStateOf(false)
        private set

    fun onEmailChange(novoValor: String) {
        email = novoValor
        emailErro = null
        errorMessage = null
    }

    fun onSenhaChange(novoValor: String) {
        senha = novoValor
        senhaErro = null
        errorMessage = null
    }

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
     * Verifica se já existe uma sessão ativa no Firebase.
     * Se existir, faz auto-login.
     */
    fun verificarSessaoExistente() {
        val user = auth.currentUser
        if (user != null) {
            usuarioLogadoUid = user.uid
            viewModelScope.launch {
                // Verificar se dados complementares existem no Room
                val usuario = repository.buscarPorEmail(user.email ?: "")
                if (usuario != null && usuario.dataNascimento == null) {
                    cadastroIncompleto = true
                }
                loginRealizado = true
            }
        }
    }

    /**
     * Login com email e senha via Firebase Auth.
     */
    fun tentarLogin() {
        errorMessage = null

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

        isLoading = true

        auth.signInWithEmailAndPassword(email, senha)
            .addOnCompleteListener { tarefa ->
                isLoading = false
                if (tarefa.isSuccessful) {
                    val user = auth.currentUser
                    usuarioLogadoUid = user?.uid

                    // Verificar se tem dados complementares no Room
                    viewModelScope.launch {
                        val usuario = repository.buscarPorEmail(email)
                        if (usuario != null && usuario.dataNascimento == null) {
                            cadastroIncompleto = true
                        }
                        loginRealizado = true
                    }
                } else {
                    errorMessage = when (tarefa.exception) {
                        is FirebaseAuthInvalidUserException -> "Conta não encontrada"
                        is FirebaseAuthInvalidCredentialsException -> "E-mail ou senha incorretos"
                        else -> "Falha na autenticação. Tente novamente."
                    }
                }
            }
    }

    /**
     * Login via Google usando o ID Token obtido pelo Credential Manager.
     */
    fun loginComGoogle(idToken: String) {
        googleLoginEmProgresso = true
        errorMessage = null

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { tarefa ->
                googleLoginEmProgresso = false
                if (tarefa.isSuccessful) {
                    val user = auth.currentUser!!
                    usuarioLogadoUid = user.uid

                    // Salvar/atualizar dados no Room
                    viewModelScope.launch {
                        val existente = repository.buscarPorEmail(user.email ?: "")
                        if (existente == null) {
                            // Primeiro login com Google — criar no Room
                            repository.salvar(
                                Usuario(
                                    uid = user.uid,
                                    nome = user.displayName ?: "Usuário",
                                    email = user.email ?: "",
                                    fotoPerfil = user.photoUrl?.toString()
                                )
                            )
                            // Dados complementares faltam
                            cadastroIncompleto = true
                        } else if (existente.dataNascimento == null) {
                            cadastroIncompleto = true
                        }
                        loginRealizado = true
                    }
                } else {
                    errorMessage = "Falha no login com Google. Tente novamente."
                }
            }
    }
}