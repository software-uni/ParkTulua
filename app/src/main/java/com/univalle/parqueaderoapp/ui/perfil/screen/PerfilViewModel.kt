package com.univalle.parqueaderoapp.ui.perfil.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.univalle.parqueaderoapp.data.supabase.SupabaseConfig
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
import okhttp3.internal.notify

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
        }
    }
    private fun guardarCambiosPerfil(nuevoNombre: String, fotoBytes: ByteArray?){
        viewModelScope.launch {
            _uiState.update { it.copy(nombre = nuevoNombre) }

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
                val resultadoFoto = usuarioRepository.sincronizarUsuario(fotoBytes)
                if (resultadoFoto.isFailure) falloFoto = true
            }

            if (resultadoNombre.isFailure || falloFoto) {
                _uiState.update {
                    it.copy(errorMensaje = "Hubo un problema al guardar algunos cambios. Intenta de nuevo.")
                }
            } else {
                cargarDatos()
            }
        }
    }
    private fun cargarDatos() {
        viewModelScope.launch {
            val usuarioActual = auth.currentUser

            val correoReal = usuarioActual?.email ?: ""
            val displayName = usuarioActual?.displayName
            val uid = usuarioActual?.uid
            val fotoUrlReal = uid?.let { usuarioRepository.obtenerUrlFotoPerfil(it) }

            val nombreReal = when {
                !displayName.isNullOrBlank() -> displayName
                correoReal.isNotBlank() -> correoReal.substringBefore("@").replaceFirstChar { it.uppercase() }
                usuarioActual?.isAnonymous == true -> "Usuario Invitado"
                else -> "Usuario"
            }

            _uiState.update {
                it.copy(
                    isLoading = true,
                    nombre = nombreReal,
                    correo = correoReal,
                    fotoUrl = fotoUrlReal,
                    errorMensaje = null
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
class PerfilViewModelFactory(
    private val auth: FirebaseAuth,
    private val vehiculoRepository: VehiculoRepository,
    private val usuarioRepository: UsuarioRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(auth, vehiculoRepository, usuarioRepository ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}