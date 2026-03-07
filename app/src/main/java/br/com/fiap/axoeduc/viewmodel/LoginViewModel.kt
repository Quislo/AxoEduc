package br.com.fiap.axoeduc.viewmodel

import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class LoginViewModel : ViewModel() {

    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var enviado by mutableStateOf(false)

    fun isFormValid(): Boolean {
        return email.isNotBlank()
                && Patterns.EMAIL_ADDRESS.matcher(email).matches()
                && senha.isNotBlank()
    }

    /**
     * Tenta realizar o login. Retorna true se o formulário for válido.
     */
    fun tentarLogin(): Boolean {
        enviado = true
        return isFormValid()
    }
}
