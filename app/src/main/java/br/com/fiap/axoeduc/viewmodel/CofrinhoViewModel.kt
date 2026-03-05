package br.com.fiap.axoeduc.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import br.com.fiap.axoeduc.model.Cofrinho
import br.com.fiap.axoeduc.repository.CofrinhoRepository

class CofrinhoViewModel(
    private val repository: CofrinhoRepository = CofrinhoRepository()
) : ViewModel() {

    private val _cofrinhos = mutableStateListOf<Cofrinho>()
    val cofrinhos: List<Cofrinho> = _cofrinhos

    init {
        carregarCofrinhos()
    }

    private fun carregarCofrinhos() {
        _cofrinhos.addAll(repository.getCofrinhos())
    }
}