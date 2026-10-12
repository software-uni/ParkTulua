package com.univalle.parqueaderoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import co.parktulua.core.designsystem.IlustracionMoradoOscuro
import co.parktulua.core.designsystem.Neutro0
import co.parktulua.core.designsystem.PrimarioOscuro
import com.univalle.parqueaderoapp.ui.components.card.FormularioRegistro
import com.univalle.parqueaderoapp.ui.componet.logo.MarcaSuperior

@Composable
fun RegistroScreen(
    viewModel: AuthViewModel,
    onVolverLoginClick: () -> Unit
){
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        PrimarioOscuro,
                        IlustracionMoradoOscuro
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 16.dp, bottom = 24.dp)
            ) {
                MarcaSuperior()
            }
            Spacer(modifier = Modifier.weight(1f))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Neutro0,
                shadowElevation = 16.dp,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, end = 24.dp, top = 36.dp, bottom = 32.dp),

                    ) {
                    FormularioRegistro(
                        nombre = viewModel.nombre,
                        onNombreChange = { viewModel.onNombreChange(it) },

                        email = viewModel.email,
                        onEmailChange = { viewModel.onEmailChange(it) },
                        emailError = viewModel.emailError,
                        onValidarEmail = {viewModel.validarEmailEnvivo()},

                        contrasena = viewModel.contrasena,
                        onContrasenaChange = { viewModel.onContrasenaChange(it) },
                        onValidarContrasena = { viewModel.validarContrasenaEnVivo() },
                        contrasenaError = viewModel.contrasenaError,

                        confirmarContrasena = viewModel.confirmarContrasena,
                        onConfirmarContrasenaChange = { viewModel.onConfirmarContrasenaChange(it) },

                        onRegistraClilck = {
                            viewModel.registrarUsuario()
                        }
                    )
                }
            }
        }
    }
}