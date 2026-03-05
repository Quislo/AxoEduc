package br.com.fiap.axoeduc.repository

import br.com.fiap.axoeduc.model.CalculadoraJuros
import kotlin.math.pow

class CalculadoraRepository {

        fun calcularJuros(dados: CalculadoraJuros): Double {

            val montante =
                dados.valorFinanciado *
                        (1 + dados.taxaMensal).pow(dados.meses)

            return montante - dados.valorFinanciado
        }

}