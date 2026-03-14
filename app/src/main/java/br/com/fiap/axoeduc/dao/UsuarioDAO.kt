package br.com.fiap.axoeduc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.axoeduc.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDAO {

    @Insert
    suspend fun salvar(usuario: Usuario): Long

    @Query("SELECT * FROM tb_usuario WHERE id = :id")
    fun buscarPorId(id: Int): Flow<Usuario?>

    @Query("SELECT * FROM tb_usuario WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    @Update
    suspend fun atualizar(usuario: Usuario)

    @Query("UPDATE tb_usuario SET nome = :nome WHERE id = :id")
    suspend fun atualizarNome(id: Int, nome: String)

    @Query("UPDATE tb_usuario SET email = :email WHERE id = :id")
    suspend fun atualizarEmail(id: Int, email: String)

    @Query("UPDATE tb_usuario SET senha = :senha WHERE id = :id")
    suspend fun atualizarSenha(id: Int, senha: String)

    @Query("SELECT senha FROM tb_usuario WHERE id = :id")
    suspend fun buscarSenhaPorId(id: Int): String?

    @Query("UPDATE tb_usuario SET fotoPerfil = :uri WHERE id = :id")
    suspend fun atualizarFotoPerfil(id: Int, uri: String?)

}