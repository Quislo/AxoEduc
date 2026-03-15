package br.com.fiap.axoeduc.model

import androidx.room.ColumnInfo
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
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "nome")
    val nome: String,

    @ColumnInfo(name = "email")
    val email: String,

    @ColumnInfo(name = "dataNascimento")
    val dataNascimento: LocalDate? = null,

    @ColumnInfo(name = "rendaMensal")
    val rendaMensal: Double = 0.0,

    @ColumnInfo(name = "fotoPerfil")
    val fotoPerfil: String? = null
)