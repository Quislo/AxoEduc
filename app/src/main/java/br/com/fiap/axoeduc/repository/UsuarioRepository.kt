package br.com.fiap.axoeduc.repository

import br.com.fiap.axoeduc.dao.UsuarioDAO
import br.com.fiap.axoeduc.model.Usuario
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class UsuarioRepository(private val dao: UsuarioDAO) {

    suspend fun cadastrar(nome: String, email: String, dataNascimento: LocalDate, rendaMensal: Double, senha: String) {

        val novo = Usuario(
            id = 0,
            nome = nome,
            email = email,
            dataNascimento = dataNascimento,
            rendaMensal = rendaMensal,
            senha = senha
        )

        dao.salvar(novo)
    }

    fun buscarPorId(id: Int): Flow<Usuario?> {
        return dao.buscarPorId(id)
    }

    suspend fun buscarPorEmail(email: String): Usuario? {
        return dao.buscarPorEmail(email)
    }

    suspend fun atualizar(usuario: Usuario) {
        dao.atualizar(usuario)
    }
}