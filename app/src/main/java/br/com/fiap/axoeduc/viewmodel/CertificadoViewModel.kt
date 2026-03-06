package br.com.fiap.axoeduc.viewmodel

import androidx.lifecycle.ViewModel
import br.com.fiap.axoeduc.model.Certificado
import br.com.fiap.axoeduc.repository.CertificadoRepository

class CertificadoViewModel : ViewModel() {

    private val repository = CertificadoRepository()

    val certificados: List<Certificado> =
        repository.listarCertificados()
}