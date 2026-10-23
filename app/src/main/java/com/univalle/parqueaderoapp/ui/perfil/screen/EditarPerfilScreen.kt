package com.univalle.parqueaderoapp.ui.perfil.screen

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.FondoApp
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioOscuro
import com.univalle.parqueaderoapp.ui.componet.botones.botones.BotonPrincipal
import com.univalle.parqueaderoapp.ui.perfil.screen.componet.DialogoEliminarCuenta
import com.univalle.parqueaderoapp.ui.perfil.screen.componet.SelectorFotoPerfil
import com.univalle.parqueaderoapp.ui.shared.inputs.CampoTextoParqueadero

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarPerfilScreen(
    nombreActual: String,
    correoActual: String,
    fotoUrlActual: String? = null,
    onGuardarClick: (nuevoNombre: String, nuevaFotoUri: Uri?) -> Unit,
    onVolverClick: () -> Unit,
    onEliminarCuentaClick: () -> Unit = {}
) {
    val (nombresInicial, apellidosInicial) = remember(nombreActual) { separarNombre(nombreActual) }
    var nombres by remember(nombreActual) { mutableStateOf(nombresInicial) }
    var apellidos by remember(nombreActual) { mutableStateOf(apellidosInicial) }
    var nuevaFotoUri by remember { mutableStateOf<Uri?>(null) }
    var mostrarDialogoEliminar by remember { mutableStateOf(false) }

    // Se guarda un solo nombre: nombres y apellidos unidos, sin espacios de más
    val nombreNuevo = listOf(nombres, apellidos)
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .joinToString(" ")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar perfil", fontWeight = FontWeight.Bold, color = PrimarioOscuro) },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = PrimarioOscuro
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoApp)
            )
        },
        containerColor = FondoApp
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SelectorFotoPerfil(
                fotoUrlActual = fotoUrlActual,
                nuevaFotoUri = nuevaFotoUri,
                onFotoSeleccionada = { nuevaFotoUri = it }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 0.5.dp,
                        color = Neutro600.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(14.dp)
                    )
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Datos personales",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Neutro600
                )

                CampoTextoParqueadero(
                    label = "Nombres",
                    value = nombres,
                    onValueChange = { nombres = it },
                    placeholder = "Nombre",
                    iconoIzquierdo = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                CampoTextoParqueadero(
                    label = "Apellidos",
                    value = apellidos,
                    onValueChange = { apellidos = it },
                    placeholder = "Apellido",
                    iconoIzquierdo = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Correo bloqueado
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Correo electrónico (no modificable)",
                    fontSize = 12.sp,
                    color = Neutro600,
                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(text = correoActual, fontSize = 16.sp, color = Neutro600)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            BotonPrincipal(
                texto = "Guardar cambios",
                onClick = { onGuardarClick(nombreNuevo, nuevaFotoUri) },
                enabled = nombres.isNotBlank() &&
                        (nombreNuevo != nombreActual.trim() || nuevaFotoUri != null)
            )

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = { mostrarDialogoEliminar = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = "Eliminar cuenta",
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (mostrarDialogoEliminar) {
        DialogoEliminarCuenta(
            onConfirmar = {
                mostrarDialogoEliminar = false
                onEliminarCuentaClick()
            },
            onCancelar = { mostrarDialogoEliminar = false }
        )
    }
}