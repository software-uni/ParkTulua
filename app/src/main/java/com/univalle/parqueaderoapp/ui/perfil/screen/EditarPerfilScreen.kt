package com.univalle.parqueaderoapp.ui.perfil.screen

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.BordeSuave
import co.parktulua.core.designsystem.FondoApp
import co.parktulua.core.designsystem.LlenoTexto
import co.parktulua.core.designsystem.Neutro100
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioOscuro
import com.univalle.parqueaderoapp.ui.perfil.screen.componet.DialogoEliminarCuenta
import com.univalle.parqueaderoapp.ui.perfil.screen.componet.SelectorFotoPerfil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(
    nombreActual: String,
    correoActual: String,
    fotoUrlActual: String?,
    onGuardarClick: (String, Uri?) -> Unit,
    mensajeAccion: String?,
    onMensajeMostrado: () -> Unit,
    onEliminarCuentaConfirmado: () -> Unit,
    onVolverClick: () -> Unit
) {
    // Lógica simple para dividir el nombre actual en Nombres y Apellidos
    val partesNombre = nombreActual.split(" ", limit = 2)
    var nombres by remember { mutableStateOf(partesNombre.getOrNull(0) ?: "") }
    var apellidos by remember { mutableStateOf(partesNombre.getOrNull(1) ?: "") }
    var nuevaFotoUri by remember { mutableStateOf<Uri?>(null) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(mensajeAccion) {
        mensajeAccion?.let {
            snackbarHostState.showSnackbar(it)
            onMensajeMostrado()
        }
    }

    val scrollState = rememberScrollState()

    Scaffold(
        snackbarHost = {SnackbarHost(snackbarHostState)},
        containerColor = FondoApp,
        topBar = {
            TopAppBar(
                title = { Text("Editar perfil", fontWeight = FontWeight.Bold, color = PrimarioOscuro) },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = PrimarioOscuro
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoApp)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))


            SelectorFotoPerfil(
                fotoUrlActual = fotoUrlActual,
                nuevaFotoUri = nuevaFotoUri,
                onFotoSeleccionada = { nuevaFotoUri = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Formulario
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Campo Nombres
                CampoTextoPerfil(
                    label = "Nombres",
                    value = nombres,
                    onValueChange = { nombres = it },
                    icono = Icons.Default.Person
                )

                // Campo Apellidos
                CampoTextoPerfil(
                    label = "Apellidos",
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    icono = Icons.Default.Person
                )

                // Campo Correo (Deshabilitado)
                CampoTextoPerfil(
                    label = "Correo electrónico (no modificable)",
                    value = correoActual,
                    onValueChange = {},
                    icono = Icons.Default.Email,
                    habilitado = false
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón Guardar
            Button(
                onClick = {
                    val nombreCompleto = "$nombres $apellidos".trim()
                    onGuardarClick(nombreCompleto, nuevaFotoUri)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Primario),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(text = "Guardar cambios", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            // Espaciador flexible para empujar el botón de eliminar hacia abajo
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(32.dp))

            // Botón Eliminar Cuenta
            TextButton(
                onClick = { mostrarDialogoEliminar = true  },
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Eliminar cuenta",
                    color = LlenoTexto,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
    if (mostrarDialogoEliminar) {
        DialogoEliminarCuenta(
            onConfirmar = {
                mostrarDialogoEliminar = false
                onEliminarCuentaConfirmado()
            },
            onCancelar = { mostrarDialogoEliminar = false }
        )
    }
}

@Composable
private fun CampoTextoPerfil(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    habilitado: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = if (habilitado) Neutro900 else Neutro600,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            enabled = habilitado,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    imageVector = icono,
                    contentDescription = null,
                    tint = if (habilitado) Neutro600 else BordeSuave
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BordeSuave,
                focusedBorderColor = Primario,
                unfocusedContainerColor = FondoApp,
                focusedContainerColor = FondoApp,
                disabledContainerColor = Neutro100,
                disabledBorderColor = BordeSuave,
                disabledTextColor = Neutro600
            ),
            singleLine = true
        )
    }
}