package br.com.fiap.axoeduc.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_credencial_email",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["usuarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["usuarioId"], unique = true)]
)
data class CredencialEmail(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val usuarioId: Int,

    val senha: String
)
