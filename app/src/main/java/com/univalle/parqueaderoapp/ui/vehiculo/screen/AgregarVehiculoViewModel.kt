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
        val (esValida, mensaje) = validarFormato(_uiState.value.placa, tipo)
        _uiState.update {
            it.copy(tipo = tipo, placaValida = esValida, errorFormatoPlaca = mensaje, errorMensaje = null)
        }
    }

    fun onPlacaChange(texto: String) {
        val limpia = texto.uppercase().filter { it in 'A'..'Z' || it in '0'..'9' }
        val (esValida, mensaje) = validarFormato(limpia, _uiState.value.tipo)
        _uiState.update {
            it.copy(placa = limpia, placaValida = esValida, errorFormatoPlaca = mensaje, errorMensaje = null)
        }
    }
    private fun validarFormato(placa: String, tipo: TipoVehiculo?): Pair<Boolean, String?> {
        if (tipo == null || !tipo.requierePlaca) return Pair(true, null)
        if (placa.isEmpty()) return Pair(false, null)

        return when (tipo.codigo.uppercase()) {
            "AUTO", "AUTOMOVIL" -> {
                if (placa.matches(Regex("^[A-Z]{3}\\d{3}$"))) Pair(true, null)
                else Pair(false, "El auto requiere 3 letras y 3 números (Ej: ABC123)")
            }
            "MOTO", "MOTOCICLETA" -> {
                if (placa.matches(Regex("^[A-Z]{3}\\d{2}[A-Z]?$"))) Pair(true, null)
                else Pair(false, "La placa de una motocicleta debe tener 3 letras, 2 números y 1 letra (ej: ABC12D)")
            }
            else -> Pair(true, null)
        }
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