package com.univalle.parqueaderoapp.data.reserva

interface ReservaRepository{
    suspend fun crearReserva(
        parqueaderoId : String,
        vehiculoId : String,
        duracionEstimadaMin : Int,
        minutosParaLlegar : Int
    ) : Result<ReservaResponse>
}