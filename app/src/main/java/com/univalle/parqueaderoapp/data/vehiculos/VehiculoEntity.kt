package com.univalle.parqueaderoapp.data.vehiculos

import com.google.gson.annotations.SerializedName

data class VehiculoEntity(
    val id: String,
    val placa: String?,
    val tipo: String
)

