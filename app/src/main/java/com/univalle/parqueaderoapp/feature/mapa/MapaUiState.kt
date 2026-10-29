package com.univalle.parqueaderoapp.feature.mapa

import com.univalle.parqueaderoapp.domain.model.Coordenada
import com.univalle.parqueaderoapp.domain.model.PermisoUbicacion

data class MapaUiState(

    val permiso: PermisoUbicacion = PermisoUbicacion.PENDIENTE,
    val gpsActivo: Boolean = true,
    val cargandoUbicacion: Boolean = false,
    val ubicacion: Coordenada? = null
)