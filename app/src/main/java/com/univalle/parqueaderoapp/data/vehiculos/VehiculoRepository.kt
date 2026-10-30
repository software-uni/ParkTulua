package com.univalle.parqueaderoapp.data.vehiculos

import com.airbnb.lottie.L


interface VehiculoRepository {
    suspend fun obtenerVehiculosUsuario(): Result<List<VehiculoEntity>>
    suspend fun registraVehiculos(tipoCodigo : String, placa : String?): Result<Unit>
    suspend fun suspenderVehiculo(vehiculoId: String): Result<Unit>
}