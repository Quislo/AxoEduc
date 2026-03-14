package br.com.fiap.axoeduc.repository

import br.com.fiap.axoeduc.dao.CofrinhoDAO
import br.com.fiap.axoeduc.model.Cofrinho
import kotlinx.coroutines.flow.Flow

class CofrinhoRepository(private val dao: CofrinhoDAO) {

    val reservas: Flow<List<Cofrinho>> = dao.listar()

    suspend fun criarReserva(nome: String, meta: Double, valorInicial: Double) {

        val novo = Cofrinho(
            id = 0,
            nome = nome,
            meta = meta,
            valorAtual = valorInicial
        )

        dao.salvar(novo)
    }

    suspend fun depositar(cofrinho: Cofrinho, valor: Double) {

        val atualizado = cofrinho.copy(
            valorAtual = cofrinho.valorAtual + valor
        )

        dao.atualizar(atualizado)
    }

    suspend fun retirar(cofrinho: Cofrinho, valor: Double) {

        val atualizado = cofrinho.copy(
            valorAtual = cofrinho.valorAtual - valor
        )

        dao.atualizar(atualizado)
    }
}