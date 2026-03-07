package br.com.fiap.axoeduc.viewmodel

import androidx.lifecycle.ViewModel
import br.com.fiap.axoeduc.repository.CofrinhoRepository

class CofrinhoViewModel : ViewModel() {

    private val repository = CofrinhoRepository()

    val reservas = repository.reservas

    fun criarReserva(nome: String, meta: Double, valorInicial: Double) {
        repository.criarReserva(nome, meta, valorInicial)
    }

    fun depositar(id: Int, valor: Double) {
        repository.depositar(id, valor)
    }

    fun retirar(id: Int, valor: Double) {
        repository.retirar(id, valor)
    }
}