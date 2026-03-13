package br.com.fiap.axoeduc.viewmodel

import androidx.lifecycle.ViewModel
import br.com.fiap.axoeduc.model.Certificado
import br.com.fiap.axoeduc.repository.CertificadoRepository
import kotlinx.coroutines.flow.StateFlow

class CertificadoViewModel : ViewModel() {

    val certificados: StateFlow<List<Certificado>> = CertificadoRepository.certificados

}