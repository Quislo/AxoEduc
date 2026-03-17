package br.com.fiap.axoeduc.viewmodel.cadastro

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.axoeduc.components.inputs.validarData
import br.com.fiap.axoeduc.model.Usuario
import br.com.fiap.axoeduc.repository.UsuarioRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CadastroViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    // Campos — Etapa 1
    var nome by mutableStateOf("")
    var dataNascimento by mutableStateOf("")

    // Campos — Etapa 2
    var rendaMensal by mutableStateOf("")

    // Campos — Etapa 3
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var confirmarSenha by mutableStateOf("")

    var consentimentoOpenFinance by mutableStateOf(false)
    var consentimentoLgpd by mutableStateOf(false)

    // Estado de controle
    var etapaAtual by mutableIntStateOf(0)
        private set
    var enviado by mutableStateOf(false)
    var direcao by mutableIntStateOf(1)
        private set
    var mostrarDialogoRenda by mutableStateOf(false)
    var mostrarPoliticaPrivacidade by mutableStateOf(false)
    var mostrarTermosUso by mutableStateOf(false)

    // Estados de erro por campo da etapa 3
    var emailErro by mutableStateOf<String?>(null)
        private set
    var senhaErro by mutableStateOf<String?>(null)
        private set
    var confirmarSenhaErro by mutableStateOf<String?>(null)
        private set

    // Estados Assíncronos
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var cadastroRealizado by mutableStateOf(false)
        private set
    var usuarioCriadoUid by mutableStateOf("")
        private set

    // Constantes
    val totalEtapas = 3
    val limiteRendaCentavos = 282400L

    val titulosEtapas = listOf(
        "Dados Pessoais",
        "Perfil Financeiro",
        "Dados de acesso"
    )

    val subtitulosEtapas = listOf(
        "Conte-nos um pouco sobre você",
        "Informação utilizada para personalizar\nsua experiência",
        "Crie sua conta para começar"
    )

    fun onEmailChange(novoValor: String) {
        email = novoValor
        emailErro = null
        enviado = false
    }

    fun onSenhaChange(novoValor: String) {
        senha = novoValor
        senhaErro = null
        enviado = false
    }

    fun onConfirmarSenhaChange(novoValor: String) {
        confirmarSenha = novoValor
        confirmarSenhaErro = null
        enviado = false
    }

    fun onEmailFocusLost() {
        if (email.isBlank()) return

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailErro = "E-mail inválido"
            return
        }

        // Verificar duplicidade no Firebase
        viewModelScope.launch {
            try {
                auth.fetchSignInMethodsForEmail(email)
                    .addOnSuccessListener { result ->
                        if (result.signInMethods?.isNotEmpty() == true) {
                            emailErro = "E-mail já cadastrado"
                        }
                    }
            } catch (_: Exception) {
                // Silenciar erros de rede na validação inline
            }
        }
    }

    fun isEtapaValida(): Boolean {
        return when (etapaAtual) {
            0 -> nome.trim().length >= 3
                    && dataNascimento.length == 8
                    && validarData(dataNascimento) == null

            1 -> rendaMensal.isNotBlank()
                    && (rendaMensal.toLongOrNull() ?: 0L) > 0

            2 -> email.isNotBlank()
                    && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                    && senha.isNotBlank()
                    && confirmarSenha.isNotBlank()
                    && senha == confirmarSenha
                    && consentimentoOpenFinance
                    && consentimentoLgpd
                    && emailErro == null
                    && senhaErro == null
                    && confirmarSenhaErro == null

            else -> false
        }
    }

    private fun validarEtapa3(): Boolean {
        emailErro = when {
            email.isBlank() -> "Campo obrigatório"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "E-mail inválido"
            else -> null
        }

        senhaErro = when {
            senha.isBlank() -> "Campo obrigatório"
            senha.length < 6 -> "Mínimo de 6 caracteres"
            else -> null
        }

        confirmarSenhaErro = when {
            confirmarSenha.isBlank() -> "Campo obrigatório"
            confirmarSenha != senha -> "As senhas não coincidem"
            else -> null
        }

        return emailErro == null && senhaErro == null && confirmarSenhaErro == null
    }

    fun avancarEtapa() {
        enviado = true
        errorMessage = null

        if (!isEtapaValida()) {
            if (etapaAtual == 2) validarEtapa3()
            return
        }

        if (etapaAtual == 1 && (rendaMensal.toLongOrNull() ?: 0L) > limiteRendaCentavos) {
            mostrarDialogoRenda = true
            return
        }

        if (etapaAtual < totalEtapas - 1) {
            direcao = 1
            enviado = false
            etapaAtual++
            return
        }

        // Última etapa válida — criar conta no Firebase + salvar no Room
        salvarUsuario()
    }

    /**
     * Cria a conta no Firebase Auth e salva dados de perfil no Room.
     */
    private fun salvarUsuario() {
        if (!validarEtapa3()) return

        isLoading = true
        errorMessage = null

        auth.createUserWithEmailAndPassword(email, senha)
            .addOnCompleteListener { tarefa ->
                if (tarefa.isSuccessful) {
                    val user = auth.currentUser!!

                    // Atualizar displayName no Firebase
                    val profileUpdates = userProfileChangeRequest {
                        displayName = nome.trim()
                    }
                    user.updateProfile(profileUpdates).addOnCompleteListener {
                        // Salvar dados no Room
                        viewModelScope.launch {
                            try {
                                val formatter = DateTimeFormatter.ofPattern("ddMMyyyy")
                                val dataNascParsed = LocalDate.parse(dataNascimento, formatter)
                                val rendaDouble = (rendaMensal.toLongOrNull() ?: 0L) / 100.0

                                repository.salvar(
                                    Usuario(
                                        uid = user.uid,
                                        nome = nome.trim(),
                                        email = email.trim(),
                                        dataNascimento = dataNascParsed,
                                        rendaMensal = rendaDouble
                                    )
                                )

                                usuarioCriadoUid = user.uid
                                cadastroRealizado = true
                            } catch (e: Exception) {
                                errorMessage = "Erro ao salvar dados: ${e.message}"
                            } finally {
                                isLoading = false
                            }
                        }
                    }
                } else {
                    isLoading = false
                    errorMessage = when (tarefa.exception) {
                        is FirebaseAuthUserCollisionException -> "E-mail já cadastrado"
                        else -> "Erro ao criar conta: ${tarefa.exception?.message}"
                    }
                }
            }
    }

    fun voltarEtapa() {
        direcao = -1
        enviado = false
        etapaAtual--
        errorMessage = null
        emailErro = null
        senhaErro = null
        confirmarSenhaErro = null
    }

    fun confirmarDialogoRenda() {
        mostrarDialogoRenda = false
        direcao = 1
        enviado = false
        etapaAtual++
    }
}