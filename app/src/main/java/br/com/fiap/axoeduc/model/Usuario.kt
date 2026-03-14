package br.com.fiap.axoeduc.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(
    tableName = "tb_usuario",
    indices = [Index(value = ["email"], unique = true)]
)
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    val nome: String,

    val email: String,

    val dataNascimento: LocalDate,

    val rendaMensal: Double,

    val senha: String,

    val fotoPerfil: String? = null
)