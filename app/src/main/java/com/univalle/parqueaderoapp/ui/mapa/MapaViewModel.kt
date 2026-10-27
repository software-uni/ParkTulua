package com.univalle.parqueaderoapp.ui.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univalle.parqueaderoapp.data.mapa.MapaUiState
import com.univalle.parqueaderoapp.data.ubicacion.PermisoUbicacion
import com.univalle.parqueaderoapp.data.ubicacion.UbicacionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepository: UbicacionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapaUiState())
    val uiState: StateFlow<MapaUiState> = _uiState.asStateFlow()

    fun onPermisoResultado(concedido: Boolean) {
        _uiState.update {
            it.copy(permiso = if (concedido) PermisoUbicacion.CONCEDIDO else PermisoUbicacion.DENEGADO)
        }
        if (concedido) refrescarUbicacion()
    }

    fun refrescarUbicacion() {
        val gps = ubicacionRepository.gpsActivo()
        _uiState.update { it.copy(gpsActivo = gps) }
        if (!gps || _uiState.value.permiso != PermisoUbicacion.CONCEDIDO) return

        viewModelScope.launch {
            _uiState.update { it.copy(cargandoUbicacion = true) }
            val resultado = ubicacionRepository.obtenerUbicacionActual()
            _uiState.update {
                it.copy(cargandoUbicacion = false, ubicacion = resultado.getOrNull() ?: it.ubicacion)
            }
        }
    }
}