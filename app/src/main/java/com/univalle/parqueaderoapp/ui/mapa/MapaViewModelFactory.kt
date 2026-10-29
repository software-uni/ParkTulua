package com.univalle.parqueaderoapp.ui.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.univalle.parqueaderoapp.domain.repository.UbicacionRepository

class MapaViewModelFactory(
    private val ubicacionRepository: UbicacionRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MapaViewModel(ubicacionRepository) as T
}