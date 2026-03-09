package br.com.fiap.axoeduc.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class PerfilViewModel : ViewModel() {

    var nome by mutableStateOf("João Costa Sales")
        private set

    var email by mutableStateOf("joaocosta@gmail.com")
        private set

    fun onNomeChange(novoNome: String) {
        nome = novoNome
    }

    fun onEmailChange(novoEmail: String) {
        email = novoEmail
    }
}