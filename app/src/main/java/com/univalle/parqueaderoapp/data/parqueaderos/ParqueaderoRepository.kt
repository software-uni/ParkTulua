package com.univalle.parqueaderoapp.data.parqueaderos

import com.univalle.parqueaderoapp.domain.model.ParqueaderoMapa

interface ParqueaderoRepository {
    suspend fun obtenerParqueaderos(): Result<List<ParqueaderoMapa>>
}

