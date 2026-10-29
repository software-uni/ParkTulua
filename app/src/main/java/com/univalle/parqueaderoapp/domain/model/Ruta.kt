package com.univalle.parqueaderoapp.domain.model

import com.univalle.parqueaderoapp.data.ubicacion.Coordenada

data class Ruta(
    val puntos: List<Coordenada>,
    val distanciaMetros: Double,
    val duracionSegundos: Double
)

sealed class RutaError : Exception() {
    class SinConexion : RutaError()
    class SinRuta : RutaError()
    class LimiteExcedido : RutaError()
    class Servicio(val codigoHttp: Int) : RutaError()
}