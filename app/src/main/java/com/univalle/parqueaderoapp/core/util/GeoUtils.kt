package com.univalle.parqueaderoapp.core.util

import com.univalle.parqueaderoapp.domain.model.Coordenada
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.pow

object GeoUtils {

    /**
     * Calcula la distancia en metro entre dos coordenadas geográficas usando la fórmula de Haversine.
     */
    fun calcularDistanciaMetros(origen: Coordenada, latitudDestino: Double, longitudDestino: Double): Double {
        val radioTierra = 6_371_000.0
        val dLat = Math.toRadians(latitudDestino - origen.latitud)
        val dLon = Math.toRadians(longitudDestino - origen.longitud)
        val h = sin(dLat / 2).pow(2) +
                cos(Math.toRadians(origen.latitud)) * cos(Math.toRadians(latitudDestino)) * sin(dLon / 2).pow(2)
        return 2 * radioTierra * asin(sqrt(h))
    }
}