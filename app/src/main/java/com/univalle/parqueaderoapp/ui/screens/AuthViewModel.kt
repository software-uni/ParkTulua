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

    var nombre by mutableStateOf("")
        private set
    var confirmarContrasena by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var errorMensaje by mutableStateOf<String?>(null)
        private set
    var emailError by mutableStateOf<String?>(null)
        private set
    var contrasenaError by mutableStateOf<String?>(null)
        private set
    var mensajeRecuperacion by mutableStateOf<String?>(null)
        private set
    var mensajeExito by mutableStateOf<String?>(null)
        private set

    fun limpiarError() {
        errorMensaje = null
    }

    fun limpiarMensajeRecuperacion() {
        mensajeRecuperacion = null
    }
    fun limpiarExito() {
        mensajeExito = null
    }


    fun onEmailChange(nuevoEmail: String) { email = nuevoEmail }
    fun onContrasenaChange(nuevaContrasena: String) { contrasena = nuevaContrasena }
    fun onNombreChange(nuevoNombre: String) { nombre = nuevoNombre }
    fun onConfirmarContrasenaChange(nuevaContrasena: String) { confirmarContrasena = nuevaContrasena }

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
                mensajeExito = "¡Éxito! Sesión iniciada."
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
                mensajeExito = "¡Éxito! Entraste con Google."
            }.onFailure { excepcion ->
                errorMensaje = excepcion.localizedMessage ?: "Error con Google"
            }
        }
    }

    fun validarEmailEnvivo(){
        val patronCorreo = android.util.Patterns.EMAIL_ADDRESS
        if(email.isBlank()){
            emailError = null
        }
        else if (!patronCorreo.matcher(email).matches()){
            emailError = "Por favor ingrese un correo válido"
        } else{
            emailError = null
        }
    }
    fun validarContrasenaEnVivo() {
        val patronContrasena = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_]).{8,}$".toRegex()
        if (contrasena.isBlank()) {
            contrasenaError = null
        } else if (!contrasena.matches(patronContrasena)) {
            contrasenaError = "Mínimo 8 caracteres, una mayúscula, un número y un símbolo."
        } else {
            contrasenaError = null
        }
    }

    fun registrarUsuario(){
        //validamos que los campos no esten vacios
        if(nombre.isBlank() || email.isBlank() || contrasena.isBlank() || confirmarContrasena.isBlank()){
            errorMensaje = "Por favor completa todos los campos."
            return
        }
        //validamos formatos de correo valido
        val patronCorreo = android.util.Patterns.EMAIL_ADDRESS
        if (!patronCorreo.matcher(email).matches()) {
            emailError = "Por favor ingresa un correo electrónico válido."
            android.util.Log.d("AuthDebug", ">>> El correo falló la validación: $email")
            return
        } else{
            emailError = null
        }
        // 3. Validar criterios de contraseña segura
        // Mínimo 8 caracteres, al menos 1 mayúscula, 1 minúscula, 1 número y 1 carácter especial
        val patronContrasena = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_]).{8,}$".toRegex()
        if (!contrasena.matches(patronContrasena)) {
            errorMensaje = "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un símbolo especial."
            return
        }

        //validamos que las contraseñas coincidan
        if (contrasena != confirmarContrasena) {
            errorMensaje = "Las contraseñas no coinciden."
            return
        }
        viewModelScope.launch {
            errorMensaje = null
            val resultado = repository.registrarConCorreo(email, contrasena, nombre)
            resultado.onSuccess {
                mensajeExito = "¡Cuenta creada con éxito!"
            }.onFailure { excepcion ->
                errorMensaje = excepcion.localizedMessage ?: "Error al registrarse"
            }
        }
    }

    fun loginInivitado(){
        viewModelScope.launch {
            errorMensaje = null
            val resultado = repository.iniciarSesionInvitado()
            resultado.onSuccess {
                mensajeExito = "¡Éxito! Entraste como invitado."
            }.onFailure { exception ->
                errorMensaje = exception.localizedMessage ?: "Error al entrar como invitado "
            }
        }
    }

    //recuperar contraseña
    fun recuperarContrasena(){
        if(email.isBlank()){
            errorMensaje = "Por favor, ingresa tu correo electrónico en el campo de texto."
            return
        }
        viewModelScope.launch {
            errorMensaje = null
            mensajeRecuperacion = null
            val resultado = repository.recuperarPassword(email)
            resultado.onSuccess {
                mensajeRecuperacion = "Correo enviado. Por favor revisa tu bandeja de entrada."
            }.onFailure { excepcion ->
                errorMensaje = excepcion.localizedMessage ?: "Error al enviar el correo"
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
