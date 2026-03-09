package br.com.fiap.axoeduc.viewmodel

import androidx.lifecycle.ViewModel
import br.com.fiap.axoeduc.screens.Curso
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CursosViewModel : ViewModel() {

    private val _cursos = MutableStateFlow<List<Curso>>(emptyList())
    val cursos: StateFlow<List<Curso>> = _cursos.asStateFlow()

    init {
        carregarCursos()
    }

    private fun carregarCursos() {
        _cursos.value = listOf(
            Curso("Meu Primeiro Orçamento", 0.0f, "https://www.youtube.com/watch?v=in0XbfQEm2A"),
            Curso("Gestão pessoal", 0.0f, "https://www.youtube.com/watch?v=SJ7-ImU4UYc"),
            Curso("Investimentos a longo prazo", 0.0f, "https://www.youtube.com/watch?v=1cJO6Z2liWA"),
            Curso("Renda Extra sem sair de casa", 0.0f, "https://www.youtube.com/watch?v=pHAOS_NAoxc")
        )
    }
    fun marcarComoConcluido(titulo: String) {
        _cursos.value = _cursos.value.map { curso ->
            if (curso.titulo == titulo) {
                curso.copy(progresso = 1.0f)
            } else {
                curso
            }
        }
    }
}