package com.univalle.parqueaderoapp.data.vehiculo

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer

@Serializable
data class Vehiculo(
    @SerialName("id")
    val id : String,
    @SerialName("placa")
    val placa : String,
    @SerialName("tipo_vehiculo_id")
    val tipoVehiculoId: String? = null,

    val tipoNombre: String = "Carro"
)