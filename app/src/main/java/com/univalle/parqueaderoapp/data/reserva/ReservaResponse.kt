package com.univalle.parqueaderoapp.data.reserva

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReservaResponse(
    @SerialName("reserva_id") val reservaId: String,
    @SerialName("vence_en") val venceEn: String,
    @SerialName("qr") val qr: String
)