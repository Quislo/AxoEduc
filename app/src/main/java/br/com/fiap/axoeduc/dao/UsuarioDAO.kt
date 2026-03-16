package br.com.fiap.axoeduc.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.axoeduc.model.Usuario
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface UsuarioDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(usuario: Usuario): Long

    @Query("SELECT * FROM tb_usuario WHERE uid = :uid")
    fun buscarPorUid(uid: String): Flow<Usuario?>

    @Query("SELECT * FROM tb_usuario WHERE email = :email LIMIT 1")
    suspend fun buscarPorEmail(email: String): Usuario?

    @Update
    suspend fun atualizar(usuario: Usuario)

    @Query("UPDATE tb_usuario SET nome = :nome WHERE uid = :uid")
    suspend fun atualizarNome(uid: String, nome: String)

    @Query("UPDATE tb_usuario SET email = :email WHERE uid = :uid")
    suspend fun atualizarEmail(uid: String, email: String)

    @Query("UPDATE tb_usuario SET fotoPerfil = :uri WHERE uid = :uid")
    suspend fun atualizarFotoPerfil(uid: String, uri: String?)

    @Query("UPDATE tb_usuario SET rendaMensal = :renda, dataNascimento = :dataNascimento WHERE uid = :uid")
    suspend fun completarCadastro(uid: String, renda: Double, dataNascimento: LocalDate)
}