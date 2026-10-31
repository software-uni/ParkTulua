package com.univalle.parqueaderoapp.ui.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.univalle.parqueaderoapp.data.parqueaderos.ParqueaderoRepository
import com.univalle.parqueaderoapp.domain.repository.UbicacionRepository
import com.univalle.parqueaderoapp.domain.usecase.CalcularParqueaderosCercanosUseCase

class MapaViewModelFactory(
    private val ubicacionRepository: UbicacionRepository,
    private val parqueaderoRepository: ParqueaderoRepository,
    private val calcularCercanos: CalcularParqueaderosCercanosUseCase
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MapaViewModel(ubicacionRepository, parqueaderoRepository, calcularCercanos) as T
}