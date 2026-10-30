package com.univalle.parqueaderoapp.ui.perfil.screen

import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import co.parktulua.core.designsystem.FondoApp
import co.parktulua.core.designsystem.PrimarioOscuro
import com.univalle.parqueaderoapp.ui.perfil.screen.componet.DialogoQuitarVehiculo
import com.univalle.parqueaderoapp.ui.perfil.screen.componet.EncabezadoPerfil
import com.univalle.parqueaderoapp.ui.perfil.screen.componet.ListaVehiculos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerfilScreen(
    estado : PerfilUiState,
    onAccion: (PerfilAccion) -> Unit
){
    var vehiculoAQuitar by remember { mutableStateOf<VehiculoUiModel?>(null) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        onAccion(PerfilAccion.RecargarDatos)
    }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(estado.mensajeAccion) {
        estado.mensajeAccion?.let {
            snackbarHostState.showSnackbar(it)
            onAccion(PerfilAccion.MensajeMostrado)
        }
    }

    val scrollState = rememberScrollState()
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = FondoApp,
        topBar = {
            TopAppBar(
                title = { Text("Mi perfil", fontWeight = FontWeight.Bold, color = PrimarioOscuro) },
                navigationIcon = {
                    // Puedes conectar este IconButton a tu NavController más adelante si es necesario
                    IconButton(onClick = { /* TODO: Acción de volver */ }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = PrimarioOscuro)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoApp)
            )
        }
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
                fotoUrl = estado.fotoUrl,
                onEditarClick = { onAccion(PerfilAccion.EditarPerfilClick) },
            )
            Spacer(modifier = Modifier.height(16.dp))

            ListaVehiculos(
                vehiculos = estado.vehiculos,
                cantidad = estado.cantidadVehiculos,
                mostrarEstadoVacio = estado.mostrarEstadoVacio,
                onAgregarVehiculoClick = { onAccion(PerfilAccion.AgregarVehiculoClick)},
                onEliminarVehiculoClick = {vehiculo -> vehiculoAQuitar = vehiculo}
            )
            Spacer(modifier = Modifier.height(16.dp))
            TextButton(
                onClick = { onAccion(PerfilAccion.CerrarSesionClick) },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Cerrar sesión",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
    vehiculoAQuitar?.let { vehiculo ->
        DialogoQuitarVehiculo(
            vehiculo = vehiculo,
            onConfirmar = {
                onAccion(PerfilAccion.QuitarVehiculo(vehiculo.id))
                vehiculoAQuitar = null
            },
            onCancelar = { vehiculoAQuitar = null }
        )
    }
}