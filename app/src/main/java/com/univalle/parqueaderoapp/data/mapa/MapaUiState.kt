package com.univalle.parqueaderoapp.data.mapa

import com.univalle.parqueaderoapp.data.ubicacion.Coordenada
import com.univalle.parqueaderoapp.data.ubicacion.PermisoUbicacion

data class MapaUiState(

    val permiso: PermisoUbicacion = PermisoUbicacion.PENDIENTE,
    val gpsActivo: Boolean = true,
    val cargandoUbicacion: Boolean = false,
    val ubicacion: Coordenada? = null
)