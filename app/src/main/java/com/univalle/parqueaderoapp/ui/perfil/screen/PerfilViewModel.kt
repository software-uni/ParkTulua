package com.univalle.parqueaderoapp.ui.perfil.screen

import com.univalle.parqueaderoapp.data.auth.AuthRepository
import com.univalle.parqueaderoapp.data.vehiculos.VehiculoRepository

class PerfilViewModel (
    private val authRepository: AuthRepository,
    private val vehiculoRepository: VehiculoRepository
)