package com.univalle.parqueaderoapp.ui.perfil.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.univalle.parqueaderoapp.data.usuario.UsuarioRepository
import com.univalle.parqueaderoapp.data.usuario.UsuarioUpdateDto
import com.univalle.parqueaderoapp.data.vehiculos.VehiculoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class PerfilViewModel(
    private val auth: FirebaseAuth,
    private val vehiculoRepository: VehiculoRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState.asStateFlow()

    init {
        cargarDatos()
    }

    fun onAccion(accion: PerfilAccion) {
        when (accion) {
            is PerfilAccion.RecargarDatos -> cargarDatos()
            is PerfilAccion.EditarPerfilClick -> {
                // Lógica de navegación o edición
            }
            is PerfilAccion.CerrarSesionClick -> {
                auth.signOut()
            }
            is PerfilAccion.AgregarVehiculoClick -> {
                // Lógica para agregar vehículo
            }
            is PerfilAccion.GuardarCambiosPerfil -> guardarCambiosPerfil(accion.nuevoNombre, accion.fotoBytes)
            is PerfilAccion.QuitarVehiculo -> quitarVehiculo(accion.vehiculoId)
        }
    }
    private fun guardarCambiosPerfil(nuevoNombre: String, fotoBytes: ByteArray?){
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMensaje = null) }

            var huboError = false

            // 1. Actualizamos Firebase Auth
            try {
                val cambio = UserProfileChangeRequest.Builder()
                    .setDisplayName(nuevoNombre)
                    .build()
                auth.currentUser?.updateProfile(cambio)?.await()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("Perfil", "No se pudo actualizar el nombre en Firebase", e)
            }

            // 2. Enviamos el nuevo nombre a Supabase
            val dto = UsuarioUpdateDto(nombreCompleto = nuevoNombre)
            val resultadoNombre = usuarioRepository.actualizarPerfil(dto)

            var falloFoto = false
            if (fotoBytes != null) {
                val resultadoFoto = usuarioRepository.actualizarFotoPerfil(fotoBytes)
                if (resultadoFoto.isFailure) falloFoto = true
            }

            if (huboError) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMensaje = "Hubo un problema al guardar algunos cambios. Intenta de nuevo."
                    )
                }
                cargarDatos()
            } else {
                cargarDatos()
            }
        }
    }
    private fun quitarVehiculo(vehiculoId: String) {
        viewModelScope.launch {
            vehiculoRepository.suspenderVehiculo(vehiculoId)
                .onSuccess { cargarDatos() }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(mensajeAccion = e.message ?: "No se pudo quitar el vehículo")
                    }
                }
        }
    }
    private fun cargarDatos() {
        viewModelScope.launch {

            _uiState.update { it.copy(isLoading = true, errorMensaje = null) }

            val usuarioActual = auth.currentUser
            val correoReal = usuarioActual?.email ?: ""
            val displayName = usuarioActual?.displayName

            val fotoUrlReal = usuarioRepository.obtenerRutaFotoPerfil().getOrNull()
                ?.let { usuarioRepository.urlFotoPerfil(it) }

            val nombreReal = when {
                !displayName.isNullOrBlank() -> displayName
                correoReal.isNotBlank() -> correoReal.substringBefore("@").replaceFirstChar { it.uppercase() }
                usuarioActual?.isAnonymous == true -> "Usuario Invitado"
                else -> "Usuario"
            }

            _uiState.update {
                it.copy(
                    nombre = nombreReal,
                    correo = correoReal,
                    fotoUrl = fotoUrlReal,
                )
            }

            // Obtenemos los vehículos desde el repositorio que ya construimos con Supabase
            val resultado = vehiculoRepository.obtenerVehiculosUsuario()

            resultado.onSuccess { listaEntidades ->
                // Mapeamos de VehiculoEntity (Data) a VehiculoUiModel (UI)
                val listaUiModels = listaEntidades.map { entity ->
                    VehiculoUiModel(
                        id = entity.id.toString(),
                        placa = entity.placa,
                        tipo = entity.tipo
                    )
                }

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        vehiculos = listaUiModels
                    )
                }
            }.onFailure { error ->
                Log.e("Perfil", "No se pudieron cargar los vehículos", error)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMensaje = "No pudimos cargar tus vehículos. Intenta de nuevo."
                    )
                }
            }
        }
    }
}
