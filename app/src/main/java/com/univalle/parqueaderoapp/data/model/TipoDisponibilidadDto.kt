package com.univalle.parqueaderoapp.data.model

import com.google.gson.annotations.SerializedName
import com.univalle.parqueaderoapp.domain.model.TipoDisponibilidad

data class TipoDisponibilidadDto(
    @SerializedName("tipo_codigo") val tipoCodigo: String?,
    @SerializedName("tipo_nombre") val tipoNombre: String?,
    @SerializedName("tarifa_hora") val tarifaHora: Double?,
    @SerializedName("capacidad") val capacidad: Int?,
    @SerializedName("libres") val libres: Int?,
    @SerializedName("reservables_disponibles") val reservablesDisponibles: Int?
)

fun TipoDisponibilidadDto.toDomainOrNull(): TipoDisponibilidad? {
    val codigo = tipoCodigo ?: return null
    val nombre = tipoNombre ?: return null
    return TipoDisponibilidad(
        tipoCodigo = codigo,
        tipoNombre = nombre,
        tarifaHora = tarifaHora,
        capacidad = capacidad ?: 0,
        libres = libres ?: 0,
        reservablesDisponibles = reservablesDisponibles ?: 0
    )
}