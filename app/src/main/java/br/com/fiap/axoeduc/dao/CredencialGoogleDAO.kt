package br.com.fiap.axoeduc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.axoeduc.model.CredencialGoogle

@Dao
interface CredencialGoogleDAO {

    @Insert
    suspend fun salvar(credencial: CredencialGoogle): Long

    @Query("SELECT * FROM tb_credencial_google WHERE usuarioId = :usuarioId LIMIT 1")
    suspend fun buscarPorUsuarioId(usuarioId: Int): CredencialGoogle?

    @Query("UPDATE tb_credencial_google SET googleId = :googleId WHERE usuarioId = :usuarioId")
    suspend fun atualizarGoogleId(usuarioId: Int, googleId: String)
}
