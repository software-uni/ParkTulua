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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.Acento
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {

        // --- 1. TÍTULOS ---
        Text(
            text = "Crea tu cuenta",
            fontWeight = FontWeight.Bold,
            fontSize = 24.sp,
            color = Neutro900
        )
        Text(
            text = "Reserva en segundos y guarda tu historial de pagos. No pedimos la placa para registrarte.",
            fontSize = 14.sp,
            color = Neutro600,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(2.dp))

        // --- 2. SELECTOR DE FOTO HORIZONTAL ---
        SelectorFotoHorizontal(
            foto = estado.foto,
            onFotoSeleccionada = { onAccion(RegistroAccion.FotoSeleccionada(it)) }
        )

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
                    placeholder = "Nombre",
                    iconoIzquierdo = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }
            Box(modifier = Modifier.weight(1f)) {
                CampoTextoParqueadero(
                    label = "Apellidos",
                    value = estado.apellido,
                    onValueChange = { onAccion(RegistroAccion.ApellidoCambio(it)) },
                    placeholder = "Apellido",
                    iconoIzquierdo = Icons.Default.Person,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )
            }
        }

        // --- 4. CORREO ELECTRÓNICO ---
        CampoTextoParqueadero(
            label = "Correo electrónico",
            value = estado.email,
            onValueChange = { onAccion(RegistroAccion.EmailCambio(it)) },
            placeholder = "tucorreo@ejemplo.com",
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

        // --- 5. CONTRASEÑA ---
        CampoTextoParqueadero(
            label = "Contraseña",
            value = estado.contrasena,
            onValueChange = { onAccion(RegistroAccion.ContrasenaCambio(it)) },
            placeholder = "Mínimo 8 caracteres",
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
            modifier = Modifier.fillMaxWidth(),
            iconoDerecho = {
                BotonVerOcultar(
                    visible = confirmarVisible,
                    onClick = { confirmarVisible = !confirmarVisible }
                )
            }
        )

        // --- 7. TÉRMINOS Y CONDICIONES ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
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

        Spacer(modifier = Modifier.height(8.dp))

        // --- 8. BOTÓN PRINCIPAL ---
        BotonPrincipal(
            texto = if (estado.isLoading) "Registrando..." else "Crear cuenta",
            onClick = { onAccion(RegistroAccion.Registrar) },
            enabled = estado.puedeRegistrar
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "¿Ya tienes cuenta? ", color = Neutro600, fontSize = 14.sp)
            Text(
                text = "Inicia sesión",
                color = Acento,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                modifier = Modifier.clickable { onVolverLoginClick() }
            )
        }

        // Margen de resguardo inferior para el teclado virtual
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun SelectorFotoHorizontal(
    foto: Uri?,
    onFotoSeleccionada: (Uri?) -> Unit
) {
    val selectorDeFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) onFotoSeleccionada(uri)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                selectorDeFoto.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                )
            },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = FondoApp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                if (foto != null) {
                    AsyncImage(
                        model = foto,
                        contentDescription = "Foto de perfil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AddAPhoto,
                        contentDescription = "Agregar foto",
                        tint = Acento,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column {
                Text(
                    text = "Agregar foto",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = Neutro900
                )
                Text(
                    text = "Opcional",
                    fontSize = 12.sp,
                    color = Neutro600
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

@Composable
private fun Modifier.alPerderFoco(accion: () -> Unit): Modifier {
    var teniaFoco by remember { mutableStateOf(false) }
    return onFocusChanged { foco ->
        if (teniaFoco && !foco.isFocused) accion()
        teniaFoco = foco.isFocused
    }
}