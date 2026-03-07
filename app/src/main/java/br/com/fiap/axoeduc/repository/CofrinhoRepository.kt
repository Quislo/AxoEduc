package br.com.fiap.axoeduc.repository

import br.com.fiap.axoeduc.model.Reserva
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CofrinhoRepository {

    private val _reservas = MutableStateFlow<List<Reserva>>(emptyList())
    val reservas: StateFlow<List<Reserva>> = _reservas

    private var nextId = 1

    fun criarReserva(nome: String, meta: Double, valorInicial: Double) {

        val novaReserva = Reserva(
            id = nextId++,
            nome = nome,
            meta = meta,
            valorAtual = valorInicial
        )

        _reservas.value = _reservas.value + novaReserva
    }

    fun depositar(id: Int, valor: Double) {

        _reservas.value = _reservas.value.map {

            if (it.id == id) {
                it.copy(valorAtual = it.valorAtual + valor)
            } else {
                it
            }
        }
    }

    fun retirar(id: Int, valor: Double) {

        _reservas.value = _reservas.value.map {

            if (it.id == id) {
                it.copy(valorAtual = it.valorAtual - valor)
            } else {
                it
            }
        }
    }
}