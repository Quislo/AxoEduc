package br.com.fiap.axoeduc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_cofrinho"
)
data class Cofrinho(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val nome: String,
    val meta: Double,
    val valorAtual: Double
)
