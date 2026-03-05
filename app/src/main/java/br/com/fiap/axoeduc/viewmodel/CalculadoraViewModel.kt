package br.com.fiap.axoeduc.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import br.com.fiap.axoeduc.model.CalculadoraJuros
import br.com.fiap.axoeduc.repository.CalculadoraRepository

class CalculadoraViewModel : ViewModel() {

    private val repository = CalculadoraRepository()

    var meses = mutableStateOf("")
        private set

    var taxa = mutableStateOf("")
        private set

    var valorFinanciado = mutableStateOf("")
        private set

    var jurosFinal = mutableStateOf<Double?>(null)
        private set

    fun onMesesChange(valor: String) {
        meses.value = valor
    }

    fun onTaxaChange(valor: String) {
        taxa.value = valor
    }

    fun onValorFinanciadoChange(valor: String) {
        valorFinanciado.value = valor
    }

    fun calcularJuros() {

        val mesesInt = meses.value.toIntOrNull() ?: 0
        val taxaDouble = (taxa.value.toDoubleOrNull() ?: 0.0) / 100
        val valorDouble = valorFinanciado.value.toDoubleOrNull() ?: 0.0

        val dados = CalculadoraJuros(
            meses = mesesInt,
            taxaMensal = taxaDouble,
            valorFinanciado = valorDouble
        )

        jurosFinal.value = repository.calcularJuros(dados)
    }
}