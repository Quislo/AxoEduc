package br.com.fiap.axoeduc.repository

import br.com.fiap.axoeduc.model.Certificado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object CertificadoRepository {

    private val _certificados = MutableStateFlow(
        listOf(
            Certificado("Meu Primeiro Orçamento", false),
            Certificado("Gestão pessoal", false),
            Certificado("Investimentos a longo prazo", false),
            Certificado("Renda Extra sem sair de casa", false)
        )
    )

    val certificados: StateFlow<List<Certificado>> = _certificados.asStateFlow()

    fun desbloquear(tituloCurso: String) {
        _certificados.value = _certificados.value.map { cert ->
            if (cert.titulo.equals(tituloCurso, ignoreCase = true)) {
                cert.copy(desbloqueado = true)
            } else {
                cert
            }
        }
    }
}