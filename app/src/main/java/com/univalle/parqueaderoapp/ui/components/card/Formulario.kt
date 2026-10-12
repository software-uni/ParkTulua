package com.univalle.parqueaderoapp.ui.components.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.parktulua.core.designsystem.Acento
import co.parktulua.core.designsystem.Neutro600
import com.univalle.parqueaderoapp.ui.components.inputs.CampoTextoParqueadero
import com.univalle.parqueaderoapp.ui.componet.botones.botones.BotonPrincipal

@Composable
fun FormularioCorreo(
    email: String,
    onEmailChange: (String) -> Unit,
    contrasena: String,
    onContrasenaChange: (String) -> Unit,
    onRecuperarPasswordClick: () -> Unit,
    onEntrarClick: () -> Unit
){
    // Estado local para alternar el ojito de la contraseña
    var passwordVisible by remember { mutableStateOf(false) }
    // Validación sencilla para encender el chulito verde de Disponible
    val correoEsValido = email.contains("@") && email.contains(".") && email.length > 5

    Column(modifier = Modifier.fillMaxWidth()) {
        CampoTextoParqueadero(
            label = "Correo electrónico",
            value = email,
            onValueChange = onEmailChange,
            placeholder = "ejemplo@gmail.com",
            iconoIzquierdo = Icons.Default.Email,
            esCompletado = correoEsValido,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )

        )
        Spacer(modifier = Modifier.height(8.dp))

        CampoTextoParqueadero(
            label = "Contraseña",
            value = contrasena,
            onValueChange = onContrasenaChange,
            placeholder = "Tu contraseña Segura",
            iconoIzquierdo = Icons.Default.Lock,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),

            iconoDerecho = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    val icono = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    Icon(
                        imageVector = icono,
                        contentDescription = "Alternar contraseña",
                        tint = Neutro600
                    )
                }
            }
        )
        TextButton(
            onClick = onRecuperarPasswordClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "¿Se te olvido la contraseña?",
                color = Acento
            )

        }
        Spacer(modifier = Modifier.height(16.dp))

        BotonPrincipal(
            texto = "Entrar",
            onClick = onEntrarClick
        )
    }
}
@Preview(showBackground = true)
@Composable
private fun FormularioCorreoPreview() {
    FormularioCorreo(
        email = "ejemplo@correo.com",
        onEmailChange = {},
        contrasena = "123456",
        onContrasenaChange = {},
        onRecuperarPasswordClick = {},
        onEntrarClick = {}
    )
}