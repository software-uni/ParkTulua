package com.univalle.parqueaderoapp.ui.mapa.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.maplibre.compose.map.MaplibreMap

@Composable
fun MapaParqueaderos(modifier: Modifier = Modifier) {
    MaplibreMap(modifier = modifier.fillMaxSize())
}