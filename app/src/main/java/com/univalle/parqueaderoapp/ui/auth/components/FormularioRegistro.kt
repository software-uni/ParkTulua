package com.univalle.parqueaderoapp.ui.auth.components

import android.net.Uri
import android.util.Patterns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import co.parktulua.core.designsystem.Disponible
import co.parktulua.core.designsystem.FondoApp
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import com.univalle.parqueaderoapp.ui.auth.screen.RegistroAccion
import com.univalle.parqueaderoapp.ui.auth.screen.RegistroUiState
import com.univalle.parqueaderoapp.ui.componet.botones.botones.BotonPrincipal
import com.univalle.parqueaderoapp.ui.shared.inputs.CampoTextoParqueadero

@Composable
fun FormularioRegistro(
    estado: RegistroUiState,
    onAccion: (RegistroAccion) -> Unit,
    onVolverLoginClick: () -> Unit
) {
    var contrasenaVisible by remember { mutableStateOf(false) }
    var confirmarVisible by remember { mutableStateOf(false) }

    val correoEsValido = Patterns.EMAIL_ADDRESS.matcher(estado.email).matches()
    val contrasenaEsValida = estado.contrasena.isNotBlank() && estado.contrasenaError == null

    Column(modifier = Modifier.fillMaxWidth()) {

        // --- 1. TÍTULOS ---
        Text(
            text = "Crea tu cuenta",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Neutro900
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Reserva en segundos y guarda tu historial de pagos. No pedimos la placa para registrarte.",
            fontSize = 14.sp,
            color = Neutro600,
            lineHeight = 20.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- 2. FOTO DE PERFIL ---
        FotoPerfil(
            foto = estado.foto,
            onFotoSeleccionada = { onAccion(RegistroAccion.FotoSeleccionada(it)) }
        )
        Spacer(modifier = Modifier.height(24.dp))

        // --- 3. NOMBRES Y APELLIDOS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier.weight(1f)) {
                CampoTextoParqueadero(
                    label = "Nombres",
                    value = estado.nombre,
                    onValueChange = { onAccion(RegistroAccion.NombreCambio(it)) },
                    placeholder = "juan",
                    iconoIzquierdo = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                CampoTextoParqueadero(
                    label = "Apellidos",
                    value = estado.apellido,
                    onValueChange = { onAccion(RegistroAccion.ApellidoCambio(it)) },
                    placeholder = "paredes",
                    iconoIzquierdo = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }
        }
        Spacer(modifier = Modifier.height(14.dp))

        // --- 4. CORREO ---
        CampoTextoParqueadero(
            label = "Correo electrónico",
            value = estado.email,
            onValueChange = { onAccion(RegistroAccion.EmailCambio(it)) },
            placeholder = "ejemplo@gmail.com",
            iconoIzquierdo = Icons.Default.Email,
            esError = estado.emailError != null,
            mensajeError = estado.emailError.orEmpty(),
            esCompletado = correoEsValido,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .alPerderFoco { onAccion(RegistroAccion.EmailPerdioFoco) }
        )
        Spacer(modifier = Modifier.height(14.dp))

        // --- 5. CONTRASEÑA ---
        CampoTextoParqueadero(
            label = "Contraseña",
            value = estado.contrasena,
            onValueChange = { onAccion(RegistroAccion.ContrasenaCambio(it)) },
            placeholder = "Tu contraseña segura",
            iconoIzquierdo = Icons.Default.Lock,
            esError = estado.contrasenaError != null,
            mensajeError = estado.contrasenaError.orEmpty(),
            esCompletado = contrasenaEsValida,
            visualTransformation = if (contrasenaVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .alPerderFoco { onAccion(RegistroAccion.ContrasenaPerdioFoco) },
            iconoDerecho = {
                BotonVerOcultar(
                    visible = contrasenaVisible,
                    onClick = { contrasenaVisible = !contrasenaVisible }
                )
            }
        )
        Spacer(modifier = Modifier.height(14.dp))

        // --- 6. CONFIRMAR CONTRASEÑA ---
        CampoTextoParqueadero(
            label = "Confirmar contraseña",
            value = estado.confirmarContrasena,
            onValueChange = { onAccion(RegistroAccion.ConfirmarContrasenaCambio(it)) },
            placeholder = "Repite tu contraseña",
            iconoIzquierdo = Icons.Default.Lock,
            esCompletado = estado.contrasenasCoinciden,
            visualTransformation = if (confirmarVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            iconoDerecho = {
                BotonVerOcultar(
                    visible = confirmarVisible,
                    onClick = { confirmarVisible = !confirmarVisible }
                )
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- 7. TÉRMINOS Y CONDICIONES ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Checkbox(
                checked = estado.aceptaTerminos,
                onCheckedChange = { onAccion(RegistroAccion.TerminosCambio(it)) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Disponible,
                    checkmarkColor = Color.White
                )
            )
            Text(
                text = "Acepto los términos de uso y la política de tratamiento de datos.",
                fontSize = 12.sp,
                color = Neutro600,
                lineHeight = 16.sp
            )
        }

        // --- 8. BOTÓN PRINCIPAL ---
        BotonPrincipal(
            texto = if (estado.isLoading) "Registrando..." else "Crear cuenta",
            onClick = { onAccion(RegistroAccion.Registrar) },
            enabled = estado.puedeRegistrar
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "¿Ya tienes cuenta? ", color = Neutro600, fontSize = 14.sp)
            Text(
                text = "Inicia sesión",
                color = Disponible,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onVolverLoginClick() }
            )
        }
    }
}

@Composable
private fun FotoPerfil(
    foto: Uri?,
    onFotoSeleccionada: (Uri?) -> Unit
) {
    val selectorDeFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onFotoSeleccionada(uri)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(FondoApp)
                .clickable {
                    selectorDeFoto.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            if (foto != null) {
                AsyncImage(
                    model = foto,
                    contentDescription = "Foto de perfil. Toca para cambiarla",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Agregar foto de perfil",
                    tint = Neutro600,
                    modifier = Modifier.size(50.dp)
                )
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = (-4).dp, y = (-4).dp)
                    .background(Disponible, CircleShape)
                    .padding(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
private fun BotonVerOcultar(
    visible: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (visible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
            contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
            tint = Neutro600
        )
    }
}

// Llama a la acción solo cuando el campo tenía el foco y lo pierde.
// Sin esto, la validación se disparaba al abrir la pantalla con los campos vacíos.
@Composable
private fun Modifier.alPerderFoco(accion: () -> Unit): Modifier {
    var teniaFoco by remember { mutableStateOf(false) }
    return onFocusChanged { foco ->
        if (teniaFoco && !foco.isFocused) accion()
        teniaFoco = foco.isFocused
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun FormularioRegistroPreview() {
    Surface(modifier = Modifier.padding(16.dp)) {
        FormularioRegistro(
            estado = RegistroUiState(
                nombre = "Juan",
                apellido = "Pérez",
                email = "juan@correo.com",
                contrasena = "Admin123*",
                confirmarContrasena = "Admin123*"
            ),
            onAccion = {},
            onVolverLoginClick = {}
        )
    }
}