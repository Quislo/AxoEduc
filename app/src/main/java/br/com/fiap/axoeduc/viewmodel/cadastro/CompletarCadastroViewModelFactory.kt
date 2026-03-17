package br.com.fiap.axoeduc.viewmodel.cadastro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.fiap.axoeduc.repository.UsuarioRepository

class CompletarCadastroViewModelFactory(
    private val usuarioUid: String,
    private val repository: UsuarioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CompletarCadastroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CompletarCadastroViewModel(usuarioUid, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
