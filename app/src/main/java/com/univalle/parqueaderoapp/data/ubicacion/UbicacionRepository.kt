package com.univalle.parqueaderoapp.data.ubicacion

interface UbicacionRepository {
    fun gpsActivo(): Boolean
    suspend fun obtenerUbicacionActual(): Result<Coordenada?>
}