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
    suspend fun salvar(usuario: Usuario)

    @Query("SELECT * FROM tb_usuario WHERE id = :id")
    fun buscarPorId(id: Int): Flow<Usuario?>

    @Query("SELECT * FROM tb_usuario WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    @Update
    suspend fun atualizar(usuario: Usuario)

}