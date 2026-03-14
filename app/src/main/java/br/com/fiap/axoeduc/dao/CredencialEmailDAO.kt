package br.com.fiap.axoeduc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.axoeduc.model.CredencialEmail

@Dao
interface CredencialEmailDAO {

    @Insert
    suspend fun salvar(credencial: CredencialEmail): Long

    @Query("SELECT * FROM tb_credencial_email WHERE usuarioId = :usuarioId LIMIT 1")
    suspend fun buscarPorUsuarioId(usuarioId: Int): CredencialEmail?

    @Query("SELECT ce.senha FROM tb_credencial_email ce INNER JOIN tb_usuario u ON ce.usuarioId = u.id WHERE u.email = :email LIMIT 1")
    suspend fun buscarSenhaPorEmail(email: String): String?

    @Query("UPDATE tb_credencial_email SET senha = :novaSenha WHERE usuarioId = :usuarioId")
    suspend fun atualizarSenha(usuarioId: Int, novaSenha: String)
}
