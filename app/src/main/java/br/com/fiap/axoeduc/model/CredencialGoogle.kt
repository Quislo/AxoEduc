package br.com.fiap.axoeduc.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_credencial_google",
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
data class CredencialGoogle(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val usuarioId: Int,

    val googleId: String
)
