package com.univalle.parqueaderoapp.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.parktulua.core.designsystem.IlustracionMoradoOscuro
import co.parktulua.core.designsystem.Neutro0
import co.parktulua.core.designsystem.PrimarioOscuro
import com.univalle.parqueaderoapp.ui.auth.components.FormularioRegistro
import com.univalle.parqueaderoapp.ui.componet.logo.MarcaSuperior
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
@Composable
fun RegistroScreen(
    viewModel: AuthViewModel,
    onVolverLoginClick: () -> Unit
){
    val context = LocalContext.current
    var aceptaTerminos by remember { mutableStateOf(false) }

    BackHandler{
        onVolverLoginClick()
    }

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
                .verticalScroll(rememberScrollState())
                .padding(bottom = 32.dp)
        ) {
            // 2 Encabezado con flecha  de retroceso  y logo
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, top = 16.dp, end = 24.dp, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                MarcaSuperior()
            }
            Spacer(modifier = Modifier.height(16.dp))
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

                        aceptaTerminos = aceptaTerminos,
                        onAceptaTerminosChange = { aceptaTerminos = it },

                        onRegistraClilck = {
                            if (aceptaTerminos) {
                                viewModel.registrarUsuario()
                            } else {
                                Toast.makeText(context, "Debes aceptar los términos y condiciones", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )

                }
            }
        }
    }
}