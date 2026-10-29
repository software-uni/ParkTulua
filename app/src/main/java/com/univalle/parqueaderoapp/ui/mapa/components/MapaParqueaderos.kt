package com.univalle.parqueaderoapp.ui.mapa.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.univalle.parqueaderoapp.core.maps.MapaConfig
import com.univalle.parqueaderoapp.data.ubicacion.Coordenada
import org.maplibre.compose.camera.CameraAnimation

import org.maplibre.compose.camera.CameraPosition
import org.maplibre.compose.camera.CameraUpdate
import org.maplibre.compose.map.MaplibreMap
import org.maplibre.compose.map.rememberMapState
import org.maplibre.compose.style.BaseStyle
import org.maplibre.spatialk.geojson.Position
import kotlin.time.Duration.Companion.seconds

@Composable
fun MapaParqueaderos(
    ubicacionUsuario: Coordenada?,
    modifier: Modifier = Modifier
) {
    val mapState = rememberMapState(
        baseStyle = BaseStyle.Uri(MapaConfig.ESTILO_OPEN_FREE_MAP),
        initialCameraPosition = CameraPosition(
            center = Position(
                latitude = MapaConfig.TULUA_LATITUD,
                longitude = MapaConfig.TULUA_LONGITUD
            ),
            zoom = MapaConfig.ZOOM_INICIAL
        )
    )

    LaunchedEffect(ubicacionUsuario) {
        ubicacionUsuario?.let {
            mapState.animateCamera(
                update = CameraUpdate(
                    center = Position(
                        latitude = it.latitud,
                        longitude = it.longitud
                    ),
                    zoom = MapaConfig.ZOOM_UBICACION_USUARIO
                ),
                animation = CameraAnimation.Fly { duration = 1.seconds },
            )
        }
    }

    MaplibreMap(
        state = mapState,
        modifier = modifier.fillMaxSize()
    )
}