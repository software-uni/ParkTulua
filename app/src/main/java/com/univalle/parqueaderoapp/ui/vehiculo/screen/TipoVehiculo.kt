package com.univalle.parqueaderoapp.ui.vehiculo.screen

enum class TipoVehiculo(val codigo: String, val nombre: String, val requierePlaca : Boolean){

    AUTOMOVIL("automovil","automovil",true),
    MOTOCICLETA("Motocicleta", "Motocicleta", true),
    BICICLETA("Bicicleta", "Bicicleta", false)
}