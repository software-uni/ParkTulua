package com.univalle.parqueaderoapp.ui.auth.screen

import android.net.Uri

data class RegistroUiState(
    val foto: Uri? = null,
    val nombre: String = "",
    val apellido: String = "",
    val email: String = "",
    val emailError: String? = null,
    val contrasena: String = "",
    val contrasenaError: String? = null,
    val confirmarContrasena: String = "",
    val aceptaTerminos: Boolean = false,
    val isLoading: Boolean = false
) {
    val contrasenasCoinciden: Boolean
        get() = contrasena.isNotBlank() && contrasena == confirmarContrasena

    val puedeRegistrar: Boolean
        get() = !isLoading && aceptaTerminos
}

sealed interface RegistroAccion {
    data class FotoSeleccionada(val uri: Uri?) : RegistroAccion
    data class NombreCambio(val valor: String) : RegistroAccion
    data class ApellidoCambio(val valor: String) : RegistroAccion
    data class EmailCambio(val valor: String) : RegistroAccion
    data object EmailPerdioFoco : RegistroAccion
    data class ContrasenaCambio(val valor: String) : RegistroAccion
    data object ContrasenaPerdioFoco : RegistroAccion
    data class ConfirmarContrasenaCambio(val valor: String) : RegistroAccion
    data class TerminosCambio(val aceptados: Boolean) : RegistroAccion
    data object Registrar : RegistroAccion
}