package com.univalle.parqueaderoapp.ui.mapa

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.univalle.parqueaderoapp.ui.mapa.components.MapaParqueaderos


@Composable
fun MapaScreen(viewModel: MapaViewModel) {
    val context = LocalContext.current
    val estado by viewModel.uiState.collectAsStateWithLifecycle()
    var permisoSolicitado by rememberSaveable { mutableStateOf(false) }

    val permisos = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado -> viewModel.onPermisoResultado(resultado.values.any { it }) }

    LaunchedEffect(Unit) {
        val yaConcedido = permisos.any {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
        when {
            yaConcedido -> viewModel.onPermisoResultado(true)
            !permisoSolicitado -> {
                permisoSolicitado = true
                launcher.launch(permisos)
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MapaParqueaderos(ubicacionUsuario = estado.ubicacion)
    }
}