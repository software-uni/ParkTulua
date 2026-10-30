package com.univalle.parqueaderoapp.ui.perfil.screen

data class VehiculoUiModel(
    val id: String,
    val placa: String?,
    val tipo: String
)
data class PerfilUiState(
    val isLoading: Boolean = true,
    val nombre: String = "",
    val correo: String = "",
    val fotoUrl: String? = null,
    val vehiculos: List<VehiculoUiModel> = emptyList(),
    val errorMensaje: String? = null,
    val mensajeAccion: String? = null
){
    val mostrarEstadoVacio: Boolean
        get() = !isLoading && vehiculos.isEmpty() && errorMensaje == null

    val cantidadVehiculos: Int
        get() = vehiculos.size

}
sealed class PerfilAccion {
    object EditarPerfilClick : PerfilAccion()
    object CerrarSesionClick : PerfilAccion()
    object AgregarVehiculoClick : PerfilAccion()
    object RecargarDatos : PerfilAccion()
    object EliminarCuentaConfirmado : PerfilAccion()
    object MensajeMostrado : PerfilAccion()

    data class QuitarVehiculo(val vehiculoId: String) : PerfilAccion()
    data class GuardarCambiosPerfil(
        val nuevoNombre: String,
        val fotoBytes: ByteArray? = null
    ) : PerfilAccion()

}