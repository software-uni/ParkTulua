package com.univalle.parqueaderoapp.domain.repository

import com.univalle.parqueaderoapp.domain.model.Coordenada

interface UbicacionRepository {
    fun gpsActivo(): Boolean
    suspend fun obtenerUbicacionActual(): Result<Coordenada?>
}