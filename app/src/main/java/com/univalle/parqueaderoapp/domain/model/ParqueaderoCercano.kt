package com.univalle.parqueaderoapp.domain.model

import com.univalle.parqueaderoapp.core.maps.MapaConfig

data class FiltroMapa(
    val tipoCodigo: String? = null,
    val soloDisponibles: Boolean = true,
    val radioMetros: Int = MapaConfig.RADIO_BUSQUEDA_M,
    val maxResultados: Int = MapaConfig.MAX_PARQUEADEROS_MAPA
)

data class ParqueaderoCercano(
    val parqueadero: ParqueaderoMapa,
    val distanciaMetros: Int,
    val tiposVisibles: List<TipoDisponibilidad>
)