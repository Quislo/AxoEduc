package br.com.fiap.axoeduc.repository

import br.com.fiap.axoeduc.dao.CredencialEmailDAO
import br.com.fiap.axoeduc.dao.CredencialGoogleDAO
import br.com.fiap.axoeduc.dao.UsuarioDAO
import br.com.fiap.axoeduc.model.CredencialEmail
import br.com.fiap.axoeduc.model.CredencialGoogle
import br.com.fiap.axoeduc.model.Usuario
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class UsuarioRepository(
    private val dao: UsuarioDAO,
    private val credencialEmailDao: CredencialEmailDAO,
    private val credencialGoogleDao: CredencialGoogleDAO
) {

    suspend fun cadastrar(
        nome: String,
        email: String,
        dataNascimento: LocalDate,
        rendaMensal: Double,
        senha: String
    ): Long {
        val novo = Usuario(
            id = 0,
            nome = nome,
            email = email,
            dataNascimento = dataNascimento,
            rendaMensal = rendaMensal
        )
        val usuarioId = dao.salvar(novo)

        credencialEmailDao.salvar(
            CredencialEmail(
                id = 0,
                usuarioId = usuarioId.toInt(),
                senha = senha)
        )

        return usuarioId
    }

    suspend fun buscarSenhaPorEmail(email: String): String? {
        return credencialEmailDao.buscarSenhaPorEmail(email)
    }

    suspend fun cadastrarOuBuscarGoogle(
        nome: String,
        email: String,
        googleId: String,
        fotoPerfil: String?
    ): Usuario {
        val existente = dao.buscarPorEmail(email)
        if (existente != null) {
            val credencialGoogle = credencialGoogleDao.buscarPorUsuarioId(existente.id)
            if (credencialGoogle == null) {
                credencialGoogleDao.salvar(
                    CredencialGoogle(
                        id = 0,
                        usuarioId = existente.id,
                        googleId = googleId
                    )
                )
            }
            return existente
        }

        val novo = Usuario(
            id = 0,
            nome = nome,
            email = email,
            fotoPerfil = fotoPerfil
        )
        val id = dao.salvar(novo)

        credencialGoogleDao.salvar(
            CredencialGoogle(
                id = 0,
                usuarioId = id.toInt(),
                googleId = googleId
            )
        )

        return novo.copy(id = id.toInt())
    }

    fun isCadastroIncompleto(usuario: Usuario): Boolean {
        return usuario.rendaMensal == 0.0 || usuario.dataNascimento == null
    }

    suspend fun completarCadastro(id: Int, renda: Double, dataNascimento: LocalDate) {
        dao.completarCadastro(id, renda, dataNascimento)
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
        val credencial = credencialEmailDao.buscarPorUsuarioId(id)
        if (credencial == null || credencial.senha != senhaAtual) {
            return Result.failure(Exception("Senha atual incorreta"))
        }
        credencialEmailDao.atualizarSenha(id, novaSenha)
        return Result.success(Unit)
    }

    suspend fun atualizarFotoPerfil(id: Int, uri: String?) {
        dao.atualizarFotoPerfil(id, uri)
    }
}