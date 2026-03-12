package br.com.fiap.axoeduc.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import br.com.fiap.axoeduc.model.Cofrinho
import kotlinx.coroutines.flow.Flow

@Dao
interface CofrinhoDAO {

    @Query("SELECT * FROM tb_cofrinho")
    fun listar(): Flow<List<Cofrinho>>

    @Insert
    suspend fun salvar(cofrinho: Cofrinho)

    @Update
    suspend fun atualizar(cofrinho: Cofrinho)

    @Delete
    suspend fun apagar(cofrinho: Cofrinho)

}