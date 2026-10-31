package com.univalle.parqueaderoapp.data.model

import com.google.gson.annotations.SerializedName
import com.univalle.parqueaderoapp.domain.model.ParqueaderoMapa

data class ParqueaderoMapaDto(
    @SerializedName("id") val id: String?,
    @SerializedName("nombre") val nombre: String?,
    @SerializedName("direccion") val direccion: String?,
    @SerializedName("latitud") val latitud: Double?,
    @SerializedName("longitud") val longitud: Double?,
    @SerializedName("calificacion_promedio") val calificacionPromedio: Double?,
    @SerializedName("calificaciones_total") val calificacionesTotal: Int?,
    @SerializedName("tarifa_desde") val tarifaDesde: Double?,
    @SerializedName("tipos") val tipos: List<TipoDisponibilidadDto>?
)

fun ParqueaderoMapaDto.toDomainOrNull(): ParqueaderoMapa? {
    val idValido = id ?: return null
    val nombreValido = nombre ?: return null
    val lat = latitud ?: return null
    val lng = longitud ?: return null
    return ParqueaderoMapa(
        id = idValido,
        nombre = nombreValido,
        direccion = direccion.orEmpty(),
        latitud = lat,
        longitud = lng,
        calificacionPromedio = calificacionPromedio,
        calificacionesTotal = calificacionesTotal ?: 0,
        tarifaDesde = tarifaDesde,
        tipos = tipos.orEmpty().mapNotNull { it.toDomainOrNull() }
    )
}