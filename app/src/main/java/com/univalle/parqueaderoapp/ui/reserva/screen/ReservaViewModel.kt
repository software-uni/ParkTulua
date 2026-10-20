package com.univalle.parqueaderoapp.ui.reserva.screen

import androidx.lifecycle.ViewModel
import com.univalle.parqueaderoapp.data.reserva.ReservaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// 1. El estado: guarda lo que el usuario elige y la respuesta de Supabase
data class ReservaUiState(
    val parqueaderoId : String = "id-del-parqueadero",
    val vehiculoId : String = "id-del-vehiculo",
    val tarifaPorHora: Double = 3500.0,

    //Lo que el usuario modifica en la pantalla
    val minutosParaLlegar: Int = 15,
    val duracionEstimadaHoras: Int = 2,

    // Lo que nos devuelve Supabase al crear la reserva
    val reservaId: String? = null,
    val venceEn: String? = null,
    val codigoQr: String? = null,

    // Control de la interfaz
    val estaCargando: Boolean = false,
    val errorMensaje: String? = null
)
class ReservaViewModel(
    private val reservaRepository: ReservaRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(ReservaUiState())
    val uiState: StateFlow<ReservaUiState> = _uiState.asStateFlow()

    fun seleccionarTiempoLlegada (minutos: Int){
        _uiState.update { it.copy(duracionEstimadaHoras = it.duracionEstimadaHoras + 1) }
    }
    fun aumentarDuracion() {
        _uiState.update { it.copy(duracionEstimadaHoras = it.duracionEstimadaHoras + 1) }
    }

    fun disminuirDuracion() {
        if (_uiState.value.duracionEstimadaHoras > 1) {
            _uiState.update { it.copy(duracionEstimadaHoras = it.duracionEstimadaHoras - 1) }
        }
    }
}