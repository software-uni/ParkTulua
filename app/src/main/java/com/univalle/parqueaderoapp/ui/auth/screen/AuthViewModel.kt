package com.univalle.parqueaderoapp.ui.auth.screen

import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.univalle.parqueaderoapp.data.auth.AuthRepository
import com.univalle.parqueaderoapp.data.usuario.FotoPerfilLocal
import com.univalle.parqueaderoapp.data.usuario.UsuarioRepository
import kotlinx.coroutines.launch

class AuthViewModel(
    private val repository: AuthRepository,
    private val usuarioRepository: UsuarioRepository,
    private val fotoLocal: FotoPerfilLocal
) : ViewModel() {


    // Estados de los campos
    var fotoUri by mutableStateOf<Uri?>(null)
        private set
    var nombre by mutableStateOf("")
        private set
    var apellido by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var contrasena by mutableStateOf("")
        private set
    var confirmarContrasena by mutableStateOf("")
        private set
    var terminosAceptados by mutableStateOf(false)
        private set

    // Estaods de ui y  validacion
    var isLoading by mutableStateOf(false)
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
    var requiereVerificacion by mutableStateOf(false)
        private set
    var correoVerificado by mutableStateOf(false)
        private set
    var introVista by mutableStateOf(false)
        private set
    fun marcarIntroVista() {
        introVista = true
    }

    fun onFotoChange(nuevaUri: Uri?) {
        fotoUri = nuevaUri
    }

    fun limpiarError() {
        errorMensaje = null
    }

    fun limpiarMensajeRecuperacion() {
        mensajeRecuperacion = null
    }

    fun limpiarExito() {
        mensajeExito = null
    }

    fun limpiarRequiereVerificacion() {
        requiereVerificacion = false
    }

    fun onNombreChange(nuevoNombre: String) {
        nombre = nuevoNombre
    }

    fun onApellidoChange(nuevoValor: String) {
        apellido = nuevoValor
    }

    fun onEmailChange(nuevoEmail: String) {
        email = nuevoEmail
    }

    fun onContrasenaChange(nuevaContrasena: String) {
        contrasena = nuevaContrasena
    }

    fun onConfirmarContrasenaChange(nuevaContrasena: String) {
        confirmarContrasena = nuevaContrasena
    }

    fun onTerminosChange(aceptados: Boolean) {
        terminosAceptados = aceptados
    }

    fun validarEmailEnvivo() {
        val patronCorreo = Patterns.EMAIL_ADDRESS
        if (email.isBlank()) {
            emailError = null
        } else if (!patronCorreo.matcher(email).matches()) {
            emailError = "Por favor ingrese un correo válido"
        } else {
            emailError = null
        }
    }

    fun validarContrasenaEnVivo() {
        val patronContrasena =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_]).{8,}$".toRegex()
        if (contrasena.isBlank()) {
            contrasenaError = null
        } else if (!contrasena.matches(patronContrasena)) {
            contrasenaError = "Mínimo 8 caracteres, una mayúscula, un número y un símbolo."
        } else {
            contrasenaError = null
        }
    }

    fun registrarUsuario() {
        //validamos que los campos no esten vacios
        if (nombre.isBlank() || email.isBlank() || contrasena.isBlank() || confirmarContrasena.isBlank()) {
            errorMensaje = "Por favor completa todos los campos obligatorios."
            return
        }
        if (!terminosAceptados) {
            errorMensaje = "Debes aceptar los términos y condiciones para continuar."
            return
        }
        validarEmailEnvivo()
        if (emailError != null) {
            errorMensaje = "Verifica el formato del correo."
            return
        }

        validarContrasenaEnVivo()
        if (contrasenaError != null) {
            errorMensaje = contrasenaError
            return
        }
        //validamos que las contraseñas coincidan
        if (contrasena != confirmarContrasena) {
            errorMensaje = "Las contraseñas no coinciden."
            return
        }

        viewModelScope.launch {
            isLoading = true
            errorMensaje = null

            //unimos el nombre  y apellidos antes de enviarlos
            val nombreCompleto = "${nombre.trim()} ${apellido.trim()}"

            val resultado = repository.registrarConCorreo(email, contrasena, nombreCompleto)
            resultado.onSuccess {
                mensajeExito = "¡Cuenta creada con éxito!"
                val uid = repository.uidActual()
                val uri = fotoUri
                if (uid != null && uri != null) fotoLocal.guardar(uid, uri)
                requiereVerificacion = true
            }.onFailure { excepcion ->
                val msj = excepcion.localizedMessage ?: ""
                errorMensaje = when {
                    msj.contains(
                        "already in use",
                        ignoreCase = true
                    ) -> "Este correo ya está registrado."

                    msj.contains("network", ignoreCase = true) -> "Sin conexión a internet."
                    else -> "Error al registrarse: $msj"
                }
            }
            isLoading = false
        }
    }

    fun comprobarVerificacion(silecioso: Boolean = false) {
        if (isLoading || correoVerificado) return
        viewModelScope.launch {
            if (!silecioso) isLoading = true
            repository.verificarCorreo()
                .onSuccess { verificado ->
                    if (verificado) {
                        sincronizarConSupabase()
                        correoVerificado = true
                    } else {
                        errorMensaje =
                            "Aún no has verificado tu correo. Revisa tu bandeja y toca el enlace."
                    }
                }
                .onFailure { errorMensaje = it.localizedMessage ?: "No se pudo comprobar" }
            isLoading = false
        }
    }

    fun reenviarVerificacion() {
        viewModelScope.launch {
            repository.reenviarVerificacion()
                .onSuccess { mensajeRecuperacion = "Te enviamos otro correo de verificación." }
                .onFailure { errorMensaje = it.localizedMessage ?: "No se pudo reenviar" }
        }
    }

    fun cerrarSesion() {
        repository.cerrarSesion()
        requiereVerificacion = false
        correoVerificado = false
    }

    fun login() {
        if (email.isBlank() || contrasena.isBlank()) {
            errorMensaje = "Por favor llena todos los campos."
            return
        }
        viewModelScope.launch {
            errorMensaje = null
            repository.iniciarSesion(email, contrasena)
                .onSuccess {
                    repository.verificarCorreo().onSuccess { verificado ->
                        if (verificado) {
                            sincronizarConSupabase()
                            mensajeExito = "¡Éxito! Sesión iniciada."
                        } else {
                            requiereVerificacion = true
                        }
                    }.onFailure { errorMensaje = it.localizedMessage ?: "Error al iniciar sesión" }

                }.onFailure { errorMensaje = it.localizedMessage ?: "Error al iniciar sesión" }
        }
    }

    fun loginConGoogle(idToken: String) {
        viewModelScope.launch {
            val resultado = repository.iniciarSesionConGoogle(idToken)
            resultado.onSuccess {
                // ¡Éxito! Aquí navegaremos a la pantalla del mapa después
                sincronizarConSupabase()
                mensajeExito = "¡Éxito! Entraste con Google."
            }.onFailure { excepcion ->
                errorMensaje = excepcion.localizedMessage ?: "Error con Google"
            }
        }
    }


    fun loginInivitado() {
        viewModelScope.launch {
            errorMensaje = null
            val resultado = repository.iniciarSesionInvitado()
            resultado.onSuccess {
                mensajeExito = "Exitor Entraste como invitado"
            }.onFailure { exception ->
                errorMensaje = exception.localizedMessage ?: "Error al entrar como invitado "
            }
        }
    }

    //recuperar contraseña
    fun recuperarContrasena() {
        if (email.isBlank()) {
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

    private suspend fun sincronizarConSupabase() {
        val uid = repository.uidActual()
        val foto = uid?.let { fotoLocal.leer(it) }
        usuarioRepository.sincronizarUsuario(foto)
            .onSuccess { if (uid != null) fotoLocal.borrar(uid) }
            .onFailure {
                // No bloqueamos al usuario: se reintenta en el próximo acceso
                Log.w("AuthSupabase", "No se pudo guardar en Supabase: ${it.message}")
            }
    }
}

// Factory para poder crear el ViewModel pasándole el repositorio
class AuthViewModelFactory(
    private val repository: AuthRepository,
    private val usuarioRepository: UsuarioRepository,
    private val fotoLocal: FotoPerfilLocal

) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AuthViewModel(repository, usuarioRepository, fotoLocal) as T
    }
}
