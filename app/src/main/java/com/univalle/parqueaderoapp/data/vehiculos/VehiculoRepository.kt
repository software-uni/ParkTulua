package com.univalle.parqueaderoapp.data.vehiculos


interface VehiculoRepository {
    suspend fun obtenerVehiculosUsuario(): Result<List<VehiculoEntity>>
}