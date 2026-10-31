package com.univalle.parqueaderoapp.ui.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.univalle.parqueaderoapp.data.parqueaderos.ParqueaderoRepository
import com.univalle.parqueaderoapp.domain.model.FiltroMapa
import com.univalle.parqueaderoapp.domain.model.ParqueaderoMapa
import com.univalle.parqueaderoapp.feature.mapa.MapaUiState
import com.univalle.parqueaderoapp.domain.model.PermisoUbicacion
import com.univalle.parqueaderoapp.domain.repository.UbicacionRepository
import com.univalle.parqueaderoapp.domain.usecase.CalcularParqueaderosCercanosUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepository: UbicacionRepository,
    private val parqueaderoRepository: ParqueaderoRepository,
    private val calcularCercanos: CalcularParqueaderosCercanosUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MapaUiState())
    val uiState: StateFlow<MapaUiState> = _uiState.asStateFlow()

    private var todosLosParqueaderos: List<ParqueaderoMapa> = emptyList()
    private val filtro = FiltroMapa()

    init {
        cargarParqueaderos()
    }

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
            recalcularCercanos()
        }
    }

    fun cargarParqueaderos() {
        viewModelScope.launch {
            _uiState.update { it.copy(cargandoParqueaderos = true, errorParqueaderos = false) }
            parqueaderoRepository.obtenerParqueaderos()
                .onSuccess { lista ->
                    todosLosParqueaderos = lista
                    _uiState.update { it.copy(cargandoParqueaderos = false) }
                    recalcularCercanos()
                }
                .onFailure {
                    _uiState.update { it.copy(cargandoParqueaderos = false, errorParqueaderos = true) }
                }
        }
    }

    fun seleccionar(parqueaderoId: String) {
        _uiState.update { estado ->
            estado.copy(seleccionado = estado.parqueaderos.firstOrNull { it.parqueadero.id == parqueaderoId })
        }
    }

    fun limpiarSeleccion() {
        _uiState.update { it.copy(seleccionado = null) }
    }

    private fun recalcularCercanos() {
        val estado = _uiState.value
        val origen = estado.ubicacion
        val cercanos = if (origen == null) emptyList()
        else calcularCercanos(todosLosParqueaderos, origen, filtro)
        val seleccionActual = estado.seleccionado?.let { sel ->
            cercanos.firstOrNull { it.parqueadero.id == sel.parqueadero.id }
        }
        _uiState.update { it.copy(parqueaderos = cercanos, seleccionado = seleccionActual) }
    }
}