package br.com.fiap.axoeduc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.fiap.axoeduc.dao.AppDatabase
import br.com.fiap.axoeduc.model.Cofrinho
import br.com.fiap.axoeduc.repository.CofrinhoRepository
import kotlinx.coroutines.launch

class CofrinhoViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase
        .getDatabase(application)
        .cofrinhoDao()

    private val repository = CofrinhoRepository(dao)

    val reservas = repository.reservas

    fun criarReserva(nome: String, meta: Double, valor: Double) {
        viewModelScope.launch {
            repository.criarReserva(nome, meta, valor)
        }
    }

    fun depositar(cofrinho: Cofrinho, valor: Double) {
        viewModelScope.launch {
            repository.depositar(cofrinho, valor)
        }
    }

    fun retirar(cofrinho: Cofrinho, valor: Double) {
        viewModelScope.launch {
            repository.retirar(cofrinho, valor)
        }
    }
}