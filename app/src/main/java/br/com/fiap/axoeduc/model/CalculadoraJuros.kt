package br.com.fiap.axoeduc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_calculadora"
)
data class CalculadoraJuros(
    @PrimaryKey(autoGenerate = true)
    val meses: Int,
    val taxaMensal: Double,
    val valorFinanciado: Double
)
