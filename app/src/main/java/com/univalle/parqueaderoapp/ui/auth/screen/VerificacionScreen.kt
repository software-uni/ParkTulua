package com.univalle.parqueaderoapp.ui.auth.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import co.parktulua.core.designsystem.Disponible
import co.parktulua.core.designsystem.FondoApp
import co.parktulua.core.designsystem.Neutro0
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import com.univalle.parqueaderoapp.ui.componet.botones.botones.BotonPrincipal
import kotlinx.coroutines.delay

private const val ESPERA_REENVIO_SEGUNDOS = 60

@Composable
fun VerificacionScreen(
    viewModel: AuthViewModel,
    onVolverLoginClick: () -> Unit,
    onContinuarClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var segundosReenvio by rememberSaveable { mutableIntStateOf(0) }

    fun salir() {
        viewModel.cerrarSesion()
        onVolverLoginClick()
    }

    BackHandler(onBack = ::salir)

    // Al volver a la app (por ejemplo desde el correo), se revisa en silencio
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.comprobarVerificacion(silecioso = true)
    }

    // Cuenta regresiva para poder reenviar
    LaunchedEffect(segundosReenvio) {
        if (segundosReenvio > 0) {
            delay(1_000)
            segundosReenvio--
        }
    }

    LaunchedEffect(viewModel.errorMensaje) {
        viewModel.errorMensaje?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limpiarError()
        }
    }
    LaunchedEffect(viewModel.mensajeRecuperacion) {
        viewModel.mensajeRecuperacion?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limpiarMensajeRecuperacion()
        }
    }

    VerificacionContenido(
        email = viewModel.email,
        verificado = viewModel.correoVerificado,
        isLoading = viewModel.isLoading,
        segundosReenvio = segundosReenvio,
        snackbarHostState = snackbarHostState,
        onYaVerifiqueClick = { viewModel.comprobarVerificacion() },
        onReenviarClick = {
            viewModel.reenviarVerificacion()
            segundosReenvio = ESPERA_REENVIO_SEGUNDOS
        },
        onVolverLoginClick = ::salir,
        onContinuarClick = onContinuarClick
    )
}

@Composable
private fun VerificacionContenido(
    email: String,
    verificado: Boolean,
    isLoading: Boolean,
    segundosReenvio: Int,
    snackbarHostState: SnackbarHostState,
    onYaVerifiqueClick: () -> Unit,
    onReenviarClick: () -> Unit,
    onVolverLoginClick: () -> Unit,
    onContinuarClick: () -> Unit
) {
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Neutro0
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(FondoApp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (verificado) Icons.Default.CheckCircle else Icons.Default.Email,
                    contentDescription = null,
                    tint = Disponible,
                    modifier = Modifier.size(44.dp)
                )
            }
            Spacer(modifier = Modifier.height(24.dp))

            if (verificado) {
                Text(
                    text = "¡Correo verificado!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Neutro900
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Tu cuenta ya está lista.",
                    fontSize = 14.sp,
                    color = Neutro600,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                BotonPrincipal(texto = "Continuar", onClick = onContinuarClick, enabled = true)
            } else {
                Text(
                    text = "Verifica tu correo",
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Neutro900
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Te enviamos un enlace a",
                    fontSize = 14.sp,
                    color = Neutro600,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = email.trim(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Neutro900,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tócalo y vuelve a la app. Si no lo ves, revisa la carpeta de spam.",
                    fontSize = 14.sp,
                    color = Neutro600,
                    lineHeight = 20.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))

                BotonPrincipal(
                    texto = if (isLoading) "Comprobando..." else "Ya verifiqué mi correo",
                    onClick = onYaVerifiqueClick,
                    enabled = !isLoading
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextButton(onClick = onReenviarClick, enabled = segundosReenvio == 0) {
                    Text(
                        text = if (segundosReenvio == 0) "Reenviar correo"
                        else "Reenviar en $segundosReenvio s"
                    )
                }
                TextButton(onClick = onVolverLoginClick) {
                    Text("Volver al inicio de sesión")
                }
            }
        }
    }
}