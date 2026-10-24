package com.univalle.parqueaderoapp.ui.vehiculo.screen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univalle.parqueaderoapp.data.vehiculos.VehiculoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AgregarVehiculoViewModel (
    private val vehiculoRepository: VehiculoRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(AgregarVehiculoUiState())
    val uiState : StateFlow<AgregarVehiculoUiState> = _uiState.asStateFlow()

    fun onTipoSeleccionado(tipo: TipoVehiculo) {
        _uiState.update { it.copy(tipo = tipo, errorMensaje = null) }
    }

    fun onPlacaChange(texto: String) {
        val limpia = texto.uppercase().filter { it in 'A'..'Z' || it in '0'..'9' }
        _uiState.update { it.copy(placa = limpia, errorMensaje = null) }
    }
    fun onGuardar() {
        val estado = _uiState.value
        val tipo = estado.tipo ?: return
        if (!estado.puedeGuardar) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMensaje = null) }
            vehiculoRepository.registraVehiculos(tipo.codigo, estado.placa.takeIf{ tipo.requierePlaca })
                .onSuccess {
                    _uiState.update { it.copy(isLoading = false, registroExitoso = true) }
                }
                .onFailure{ e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMensaje = e.message ?: "No se pudo registrar el vehículo"
                        )
                    }
                }
        }
    }
}