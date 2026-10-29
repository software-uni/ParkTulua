package com.univalle.parqueaderoapp.domain.model

data class TipoDisponibilidad(
    val tipoCodigo: String,
    val tipoNombre: String,
    val tarifaHora: Double?,
    val capacidad: Int,
    val libres: Int,
    val reservablesDisponibles: Int
)

data class ParqueaderoMapa(
    val id: String,
    val nombre: String,
    val direccion: String,
    val latitud: Double,
    val longitud: Double,
    val calificacionPromedio: Double?,
    val calificacionesTotal: Int,
    val tarifaDesde: Double?,
    val tipos: List<TipoDisponibilidad>
)