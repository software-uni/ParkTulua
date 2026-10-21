package com.univalle.parqueaderoapp.data.vehiculos

import com.google.gson.annotations.SerializedName

data class UsuarioVehiculoDto(
    val vehiculo: VehiculoDto?
)
data class VehiculoDto(
    val id: String?,
    val placa: String?,
    @SerializedName("tipo_vehiculo") val tipoVehiculo: TipoVehiculoDto?
)

data class TipoVehiculoDto(
    val nombre: String
)