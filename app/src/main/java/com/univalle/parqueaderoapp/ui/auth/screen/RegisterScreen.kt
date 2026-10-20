package com.univalle.parqueaderoapp.ui.auth.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import co.parktulua.core.designsystem.Neutro0
import com.univalle.parqueaderoapp.ui.auth.components.FormularioRegistro

// Mismo nombre y parámetros de siempre: AppNavGraph no cambia.
@Composable
fun RegistroScreen(
    viewModel: AuthViewModel,
    onVolverLoginClick: () -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }

    BackHandler(onBack = onVolverLoginClick)


    LaunchedEffect(viewModel.errorMensaje) {
        viewModel.errorMensaje?.let { error ->
            snackbarHostState.showSnackbar(error)
            viewModel.limpiarError()
        }
    }

    RegistroContenido(
        estado = RegistroUiState(
            foto = viewModel.fotoUri,
            nombre = viewModel.nombre,
            apellido = viewModel.apellido,
            email = viewModel.email,
            emailError = viewModel.emailError,
            contrasena = viewModel.contrasena,
            contrasenaError = viewModel.contrasenaError,
            confirmarContrasena = viewModel.confirmarContrasena,
            aceptaTerminos = viewModel.terminosAceptados,
            isLoading = viewModel.isLoading
        ),
        snackbarHostState = snackbarHostState,
        onAccion = { accion ->
            when (accion) {
                is RegistroAccion.FotoSeleccionada -> viewModel.onFotoChange(accion.uri)
                is RegistroAccion.NombreCambio -> viewModel.onNombreChange(accion.valor)
                is RegistroAccion.ApellidoCambio -> viewModel.onApellidoChange(accion.valor)
                is RegistroAccion.EmailCambio -> viewModel.onEmailChange(accion.valor)
                RegistroAccion.EmailPerdioFoco -> viewModel.validarEmailEnvivo()
                is RegistroAccion.ContrasenaCambio -> viewModel.onContrasenaChange(accion.valor)
                RegistroAccion.ContrasenaPerdioFoco -> viewModel.validarContrasenaEnVivo()
                is RegistroAccion.ConfirmarContrasenaCambio -> viewModel.onConfirmarContrasenaChange(accion.valor)
                is RegistroAccion.TerminosCambio -> viewModel.onTerminosChange(accion.aceptados)
                RegistroAccion.Registrar -> viewModel.registrarUsuario()
            }
        },
        onVolverLoginClick = onVolverLoginClick
    )
}

// Solo dibuja: no conoce el ViewModel.
@Composable
private fun RegistroContenido(
    estado: RegistroUiState,
    snackbarHostState: SnackbarHostState,
    onAccion: (RegistroAccion) -> Unit,
    onVolverLoginClick: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Neutro0
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .imePadding()
                .padding(start = 24.dp, end = 24.dp, top = 36.dp, bottom = 32.dp)
        ) {
            FormularioRegistro(
                estado = estado,
                onAccion = onAccion,
                onVolverLoginClick = onVolverLoginClick
            )
        }
    }
}