package br.com.fiap.axoeduc.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.axoeduc.repository.UsuarioRepository

class PerfilViewModelFactory(
    private val repository: UsuarioRepository,
    private val usuarioUid: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(repository, usuarioUid) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}