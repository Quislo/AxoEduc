package br.com.fiap.axoeduc.repository

import br.com.fiap.axoeduc.model.Certificado

class CertificadoRepository {

    fun listarCertificados(): List<Certificado> {

        return listOf(
            Certificado("Gestão Pessoal", true),
            Certificado("Meu primeiro orçamento", false),
            Certificado("Investimento a longo prazo", false),
            Certificado("Renda extra sem sair de casa", false)
        )
    }
}