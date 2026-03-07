package br.com.fiap.axoeduc.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import br.com.fiap.axoeduc.components.validarData

class CadastroViewModel : ViewModel() {

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

            else -> false
        }
    }

    /**
     * Tenta avançar para a próxima etapa.
     * Retorna true se o cadastro foi finalizado (última etapa válida).
     */
    fun avancarEtapa(): Boolean {
        enviado = true
        if (!isEtapaValida()) return false

        if (etapaAtual == 1 && (rendaMensal.toLongOrNull() ?: 0L) > limiteRendaCentavos) {
            mostrarDialogoRenda = true
            return false
        }

        if (etapaAtual < totalEtapas - 1) {
            direcao = 1
            enviado = false
            etapaAtual++
            return false
        }

        // Última etapa válida — cadastro finalizado
        return true
    }

    fun voltarEtapa() {
        direcao = -1
        enviado = false
        etapaAtual--
    }

    fun confirmarDialogoRenda() {
        mostrarDialogoRenda = false
        direcao = 1
        enviado = false
        etapaAtual++
    }
}
