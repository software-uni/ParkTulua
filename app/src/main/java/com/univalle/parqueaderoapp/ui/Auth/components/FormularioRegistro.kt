package com.univalle.parqueaderoapp.ui.Auth.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import com.univalle.parqueaderoapp.ui.shared.inputs.CampoTextoParqueadero
import com.univalle.parqueaderoapp.ui.componet.botones.botones.BotonPrincipal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import co.parktulua.core.designsystem.Disponible

@Composable
fun FormularioRegistro(
    nombre: String,
    onNombreChange: (String) -> Unit,

    email: String,
    onEmailChange: (String) -> Unit,
    emailError: String? = null,
    onValidarEmail: () -> Unit = {},

    contrasenaError: String? = null,
    onValidarContrasena: () -> Unit = {},
    contrasena: String,
    onContrasenaChange: (String) -> Unit,
    confirmarContrasena: String,
    onConfirmarContrasenaChange: (String) -> Unit,

    // --- NUEVO: Estado para los términos ---
    aceptaTerminos: Boolean,
    onAceptaTerminosChange: (Boolean) -> Unit,

    onRegistraClilck: () -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmarPasswordVisible by remember { mutableStateOf(false) }

    val correoEsValido = email.contains("@") && email.contains(".") && email.length > 5
    val contrasenaEsValida = contrasena.isNotBlank() && contrasenaError == null
    val lasContrasenasCoinciden = contrasena.isNotBlank() && contrasena == confirmarContrasena

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

        // --- 2. CAMPO NOMBRE ---
        CampoTextoParqueadero(
            label = "Nombre completo",
            value = nombre,
            onValueChange = onNombreChange,
            placeholder = "Tu nombre completo",
            iconoIzquierdo = Icons.Default.Person,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(14.dp))

        // --- 3. CAMPO CORREO ---
        CampoTextoParqueadero(
            label = "Correo electrónico",
            value = email,
            onValueChange = onEmailChange,
            placeholder = "nombre@parqueadero.com",
            iconoIzquierdo = Icons.Default.Email,
            esError = emailError != null,
            mensajeError = emailError ?: "",
            esCompletado = correoEsValido,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        onValidarEmail()
                    }
                }
        )
        Spacer(modifier = Modifier.height(14.dp))

        // --- 4. CAMPO CONTRASEÑA ---
        CampoTextoParqueadero(
            label = "Contraseña",
            value = contrasena,
            onValueChange = onContrasenaChange,
            placeholder = "Contr@seña Segura",
            iconoIzquierdo = Icons.Default.Lock,
            esError = contrasenaError != null,
            mensajeError = contrasenaError ?: "",
            esCompletado = contrasenaEsValida,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        onValidarContrasena()
                    }
                },
            iconoDerecho = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    val icono = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    Icon(imageVector = icono, contentDescription = "Ver contraseña", tint = Neutro600)
                }
            }
        )
        Spacer(modifier = Modifier.height(14.dp))

        // --- 5. CAMPO CONFIRMAR CONTRASEÑA ---
        CampoTextoParqueadero(
            label = "Confirmar contraseña",
            value = confirmarContrasena,
            onValueChange = onConfirmarContrasenaChange,
            placeholder = "Contr@seña Segura",
            iconoIzquierdo = Icons.Default.Lock,
            esCompletado = lasContrasenasCoinciden,
            visualTransformation = if (confirmarPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            iconoDerecho = {
                IconButton(onClick = { confirmarPasswordVisible = !confirmarPasswordVisible }) {
                    val icono = if (confirmarPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    Icon(imageVector = icono, contentDescription = "Ver contraseña", tint = Neutro600)
                }
            }
        )
        Spacer(modifier = Modifier.height(16.dp))

        // --- 6. CHECKBOX TÉRMINOS Y CONDICIONES ---
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Checkbox(
                checked = aceptaTerminos,
                onCheckedChange = onAceptaTerminosChange,
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

        // --- 7. BOTÓN PRINCIPAL ---
        BotonPrincipal(
            texto = "Crear cuenta",
            onClick = onRegistraClilck
        )
    }
}
