package br.com.fiap.axoeduc.repository

import br.com.fiap.axoeduc.dao.UsuarioDAO
import br.com.fiap.axoeduc.model.Usuario
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class UsuarioRepository(
    private val dao: UsuarioDAO
) {

    suspend fun salvar(usuario: Usuario): Long {
        return dao.salvar(usuario)
    }

    fun buscarPorUid(uid: String): Flow<Usuario?> {
        return dao.buscarPorUid(uid)
    }

    suspend fun buscarPorEmail(email: String): Usuario? {
        return dao.buscarPorEmail(email)
    }

    suspend fun atualizar(usuario: Usuario) {
        dao.atualizar(usuario)
    }

    suspend fun atualizarNome(uid: String, nome: String) {
        dao.atualizarNome(uid, nome)
    }

    suspend fun atualizarEmail(uid: String, email: String): Result<Unit> {
        val existente = dao.buscarPorEmail(email)
        if (existente != null && existente.uid != uid) {
            return Result.failure(Exception("E-mail já vinculado a outra conta"))
        }
        dao.atualizarEmail(uid, email)
        return Result.success(Unit)
    }

    suspend fun atualizarFotoPerfil(uid: String, uri: String?) {
        dao.atualizarFotoPerfil(uid, uri)
    }

    suspend fun completarCadastro(uid: String, renda: Double, dataNascimento: LocalDate) {
        dao.completarCadastro(uid, renda, dataNascimento)
    }
}