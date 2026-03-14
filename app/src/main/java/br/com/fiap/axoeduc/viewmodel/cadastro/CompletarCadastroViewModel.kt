package br.com.fiap.axoeduc.viewmodel.cadastro

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

class CompletarCadastroViewModel(
    private val repository: UsuarioRepository,
    private val usuarioId: Int
) : ViewModel() {

    // Campos
    var dataNascimento by mutableStateOf("")
    var rendaMensal by mutableStateOf("")

    // Estado de controle
    var etapaAtual by mutableIntStateOf(0)
        private set
    var enviado by mutableStateOf(false)
    var direcao by mutableIntStateOf(1)
        private set
    var mostrarDialogoRenda by mutableStateOf(false)

    // Estados Assíncronos
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set
    var cadastroCompleto by mutableStateOf(false)
        private set

    // Constantes
    val totalEtapas = 2
    val limiteRendaCentavos = 282400L

    val titulosEtapas = listOf(
        "Dados Pessoais",
        "Perfil Financeiro"
    )

    val subtitulosEtapas = listOf(
        "Precisamos da sua data de nascimento",
        "Informação utilizada para personalizar\nsua experiência"
    )

    fun isEtapaValida(): Boolean {
        return when (etapaAtual) {
            0 -> dataNascimento.length == 8
                    && validarData(dataNascimento) == null

            1 -> rendaMensal.isNotBlank()
                    && (rendaMensal.toLongOrNull() ?: 0L) > 0

            else -> false
        }
    }

    fun avancarEtapa() {
        enviado = true
        errorMessage = null

        if (!isEtapaValida()) return

        if (etapaAtual == 0) {
            direcao = 1
            enviado = false
            etapaAtual++
            return
        }

        if (etapaAtual == 1 && (rendaMensal.toLongOrNull() ?: 0L) > limiteRendaCentavos) {
            mostrarDialogoRenda = true
            return
        }

        // Última etapa válida — salvar
        salvarDados()
    }

    fun voltarEtapa() {
        direcao = -1
        enviado = false
        etapaAtual--
        errorMessage = null
    }

    fun confirmarDialogoRenda() {
        mostrarDialogoRenda = false
        salvarDados()
    }

    private fun salvarDados() {
        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                val dataFormatada = LocalDate.parse(
                    dataNascimento,
                    DateTimeFormatter.ofPattern("ddMMyyyy")
                )
                val rendaDouble = (rendaMensal.toLongOrNull() ?: 0L) / 100.0

                repository.completarCadastro(usuarioId, rendaDouble, dataFormatada)
                cadastroCompleto = true

            } catch (e: Exception) {
                errorMessage = "Erro ao salvar dados: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}
