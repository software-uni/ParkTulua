package com.univalle.parqueaderoapp.ui.perfil.screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.univalle.parqueaderoapp.data.vehiculos.VehiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PerfilViewModel(
    private val auth: FirebaseAuth, // Usamos FirebaseAuth directamente como lo venías manejando en el repositorio
    private val vehiculoRepository: VehiculoRepository
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
        }
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            val usuarioActual = auth.currentUser

            val correoReal = usuarioActual?.email ?: ""
            val displayName = usuarioActual?.displayName
            val fotoUrlReal = usuarioActual?.photoUrl?.toString()
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
    private val vehiculoRepository: VehiculoRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerfilViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerfilViewModel(auth, vehiculoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}