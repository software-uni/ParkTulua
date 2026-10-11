package com.univalle.parqueaderoapp.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.univalle.parqueaderoapp.data.auth.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    var email by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var errorMensaje by mutableStateOf<String?>(null)
        private set

    fun onEmailChange(nuevoEmail: String) { email = nuevoEmail }
    fun onContrasenaChange(nuevaContrasena: String) { contrasena = nuevaContrasena }

    fun login() {
        if (email.isBlank() || contrasena.isBlank()) {
            errorMensaje = "Por favor llena todos los campos."
            return
        }
        viewModelScope.launch {
            errorMensaje = null
            val resultado = repository.iniciarSesion(email, contrasena)
            resultado.onSuccess {
                // TODO: Navegar a pantalla principal
                errorMensaje = "¡Éxito! Sesión iniciada."
            }.onFailure { excepcion ->
                errorMensaje = excepcion.localizedMessage ?: "Error al iniciar sesión"
            }
        }
    }
    fun loginConGoogle(idToken: String) {
        viewModelScope.launch {
            val resultado = repository.iniciarSesionConGoogle(idToken)
            resultado.onSuccess {
                // ¡Éxito! Aquí navegaremos a la pantalla del mapa después
                errorMensaje = "¡Éxito! Entraste con Google."
            }.onFailure { excepcion ->
                errorMensaje = excepcion.localizedMessage ?: "Error con Google"
            }
        }
    }
}

// Factory para poder crear el ViewModel pasándole el repositorio
class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repository) as T
    }
}
