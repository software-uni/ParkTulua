package com.univalle.parqueaderoapp.data.usuario

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

data class UsuarioUpdateDto(
    @SerializedName("nombre_completo")
    val nombreCompleto : String,
    @SerializedName("foto_url")
    val fotoUrl : String ?= null
)