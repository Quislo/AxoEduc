package br.com.fiap.axoeduc.repository

import br.com.fiap.axoeduc.dao.UsuarioDAO
import br.com.fiap.axoeduc.model.Usuario
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class UsuarioRepository(private val dao: UsuarioDAO) {

    suspend fun cadastrar(nome: String, email: String, dataNascimento: LocalDate, rendaMensal: Double, senha: String): Long {

        val novo = Usuario(
            id = 0,
            nome = nome,
            email = email,
            dataNascimento = dataNascimento,
            rendaMensal = rendaMensal,
            senha = senha
        )

        return dao.salvar(novo)
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

    suspend fun atualizarNome(id: Int, nome: String) {
        dao.atualizarNome(id, nome)
    }

    suspend fun atualizarEmail(id: Int, email: String): Result<Unit> {
        val existente = dao.buscarPorEmail(email)
        if (existente != null && existente.id != id) {
            return Result.failure(Exception("E-mail já vinculado a outra conta"))
        }
        dao.atualizarEmail(id, email)
        return Result.success(Unit)
    }

    suspend fun atualizarSenha(id: Int, senhaAtual: String, novaSenha: String): Result<Unit> {
        val senhaBD = dao.buscarSenhaPorId(id)
        if (senhaBD != senhaAtual) {
            return Result.failure(Exception("Senha atual incorreta"))
        }
        dao.atualizarSenha(id, novaSenha)
        return Result.success(Unit)
    }

    suspend fun atualizarFotoPerfil(id: Int, uri: String?) {
        dao.atualizarFotoPerfil(id, uri)
    }
}