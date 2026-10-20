package com.univalle.parqueaderoapp.ui.perfil.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import co.parktulua.core.designsystem.FondoApp
import com.univalle.parqueaderoapp.ui.perfil.screen.componet.EncabezadoPerfil
import com.univalle.parqueaderoapp.ui.perfil.screen.componet.ListaVehiculos

@Composable
fun PerfilScreen(
    estado : PerfilUiState,
    onAccion: (PerfilAccion) -> Unit
){
    val scrollState = rememberScrollState()
    Scaffold(
        containerColor = FondoApp
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
        ) {
            EncabezadoPerfil(
                nombreCompleto = estado.nombre,
                correo = estado.correo,
                onEditarClick = { onAccion(PerfilAccion.EditarPerfilClick) },
                onCerrarSesionClick = { onAccion(PerfilAccion.CerrarSesionClick) }
            )

            ListaVehiculos(
                vehiculos = estado.vehiculos,
                cantidad = estado.cantidadVehiculos,
                mostrarEstadoVacio = estado.mostrarEstadoVacio,
                onAgregarVehiculoClick = { onAccion(PerfilAccion.AgregarVehiculoClick) }
            )
        }
    }
}