package com.univalle.parqueaderoapp.domain.repository

import com.univalle.parqueaderoapp.data.ubicacion.Coordenada
import com.univalle.parqueaderoapp.domain.model.Ruta

interface RutaRepository {
    suspend fun calcular(origen: Coordenada, destino: Coordenada): Result<Ruta>
}