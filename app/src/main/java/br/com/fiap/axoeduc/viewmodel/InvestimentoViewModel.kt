package br.com.fiap.axoeduc.viewmodel

import androidx.lifecycle.ViewModel
import br.com.fiap.axoeduc.model.Investimento
import br.com.fiap.axoeduc.repository.InvestimentoRepository

class InvestimentoViewModel : ViewModel() {

    private val repository = InvestimentoRepository()

    val investimentos: List<Investimento> =
        repository.listarInvestimentos()
}