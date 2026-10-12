package com.univalle.parqueaderoapp.ui.components.botones.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import co.parktulua.core.designsystem.Neutro600
import com.univalle.parqueaderoapp.ui.components.botones.inputs.CampoTextoParqueadero
import com.univalle.parqueaderoapp.ui.componet.botones.botones.BotonPrincipal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

@Composable
fun FormularioRegistro(
    nombre: String,
    onNombreChange :(String)-> Unit,
    email : String,
    onEmailChange : (String)-> Unit,
    contrasena : String,
    onContrasenaChange : (String)-> Unit,
    confirmarContrasena: String,
    onConfirmarContrasenaChange : (String)-> Unit,
    onRegistraClilck: ()-> Unit
){
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmarPasswordVisible by remember { mutableStateOf(false) }

    val correoEsValido = email.contains("@") && email.contains(".") && email.length > 5
    val lasContrasenasCoinciden = contrasena.isNotBlank() && contrasena == confirmarContrasena

    Column(modifier = Modifier.fillMaxWidth()) {

        //campo del nombre
        CampoTextoParqueadero(
            label = "Nombre completo",
            value = nombre,
            onValueChange = onNombreChange,
            placeholder = "Tu nombre completo",
            iconoIzquierdo = Icons.Default.Person,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )
        Spacer(modifier = Modifier.height(14.dp))

        // --- 2. CAMPO CORREO ---
        CampoTextoParqueadero(
            label = "Correo electrónico",
            value = email,
            onValueChange = onEmailChange,
            placeholder = "nombre@parqueadero.com",
            iconoIzquierdo = Icons.Default.Email,
            esCompletado = correoEsValido,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )
        CampoTextoParqueadero(
            label = "Contraseña",
            value = contrasena,
            onValueChange = onContrasenaChange,
            placeholder = "Contr@seña Segura",
            iconoIzquierdo = Icons.Default.Lock,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            iconoDerecho = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    val icono = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff
                    Icon(imageVector = icono, contentDescription = "Ver contraseña", tint = Neutro600)
                }
            }
        )
        Spacer(modifier = Modifier.height(14.dp))
        CampoTextoParqueadero(
            label = "Confirmar contraseña",
            value = confirmarContrasena,
            onValueChange = onConfirmarContrasenaChange,
            placeholder = "Contr@seña Segura",
            iconoIzquierdo = Icons.Default.Lock,
            esCompletado = lasContrasenasCoinciden,
            visualTransformation = if(confirmarPasswordVisible) VisualTransformation.None else  PasswordVisualTransformation(),
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
        Spacer(modifier = Modifier.height(24.dp))

        BotonPrincipal(
            texto = "Crear cuenta",
            onClick = onRegistraClilck
        )
    }
}