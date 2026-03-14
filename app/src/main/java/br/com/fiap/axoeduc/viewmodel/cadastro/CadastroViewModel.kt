package br.com.fiap.axoeduc.viewmodel.cadastro

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.axoeduc.components.inputs.validarData
import br.com.fiap.axoeduc.repository.UsuarioRepository
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CadastroViewModel(private val repository: UsuarioRepository) : ViewModel() {

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

    // Estados de erro por campo da etapa 3 — controlados pelo ViewModel
    var emailErro by mutableStateOf<String?>(null)
        private set
    var senhaErro by mutableStateOf<String?>(null)
        private set
    var confirmarSenhaErro by mutableStateOf<String?>(null)
        private set

    // Estados Assíncronos (Banco de Dados)
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var cadastroRealizado by mutableStateOf(false)
        private set
    var usuarioCriadoId by mutableIntStateOf(0)
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

    /** Callbacks de change — limpam erro do campo ao digitar. */
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

    /** Chamado pelo `onFocusLost` do EmailInput — valida formato + duplicidade. */
    fun onEmailFocusLost() {
        if (email.isBlank()) return

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailErro = "E-mail inválido"
            return
        }

        // Formato OK — verifica duplicidade no banco (Room local, latência desprezível)
        viewModelScope.launch {
            val usuarioExistente = repository.buscarPorEmail(email.trim())
            if (usuarioExistente != null) {
                emailErro = "E-mail já vinculado a uma conta existente"
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

    /**
     * Valida os campos da etapa 3 e popula os estados de erro por campo.
     * Retorna `true` se todos os campos estão válidos.
     */
    private fun validarEtapa3(): Boolean {
        emailErro = when {
            email.isBlank() -> "Campo obrigatório"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "E-mail inválido"
            else -> null
        }

        senhaErro = when {
            senha.isBlank() -> "Campo obrigatório"
            else -> null
        }

        confirmarSenhaErro = when {
            confirmarSenha.isBlank() -> "Campo obrigatório"
            confirmarSenha != senha -> "As senhas não coincidem"
            else -> null
        }

        return emailErro == null && senhaErro == null && confirmarSenhaErro == null
    }

    /**
     * Tenta avançar para a próxima etapa.
     * Na última etapa, realiza a requisição ao repositório via Coroutines.
     */
    fun avancarEtapa() {
        enviado = true
        errorMessage = null

        if (!isEtapaValida()) {
            // Se está na etapa 3, popula erros por campo
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

        // Última etapa válida — Iniciar persistência no banco
        salvarUsuario()
    }

    private fun salvarUsuario() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                // 1. Verifica duplicidade de E-mail
                val usuarioExistente = repository.buscarPorEmail(email.trim())
                if (usuarioExistente != null) {
                    emailErro = "E-mail já vinculado a uma conta existente"
                    return@launch
                }

                // 2. Formata Dados
                // dataNascimento vem como "DDMMAAAA", formatamos para LocalDate
                val dataFormatada = LocalDate.parse(
                    dataNascimento,
                    DateTimeFormatter.ofPattern("ddMMyyyy")
                )

                // rendaMensal vem em centavos, convertemos para Double (Reais)
                val rendaDouble = (rendaMensal.toLongOrNull() ?: 0L) / 100.0

                // 3. Salva no banco de dados local
                val novoId = repository.cadastrar(
                    nome = nome.trim(),
                    email = email.trim(),
                    dataNascimento = dataFormatada,
                    rendaMensal = rendaDouble,
                    senha = senha
                )

                // 4. Emite Sucesso para a View navegar
                usuarioCriadoId = novoId.toInt()
                cadastroRealizado = true

            } catch (e: Exception) {
                errorMessage = "Erro ao criar conta: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun voltarEtapa() {
        direcao = -1
        enviado = false
        etapaAtual--
        errorMessage = null
        // Limpa erros da etapa 3 ao voltar
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