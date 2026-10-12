package com.univalle.parqueaderoapp.ui.components.botones.inputs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.Acento
import co.parktulua.core.designsystem.Disponible
import co.parktulua.core.designsystem.Lleno
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario

@Composable
fun CampoTextoParqueadero(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    iconoIzquierdo: ImageVector,
    modifier: Modifier = Modifier,
    iconoDerecho: @Composable (() -> Unit)? = null,
    esError: Boolean = false,
    mensajeError: String = "",
    esCompletado: Boolean = false, // Nuevo parámetro para el chulito verde
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
){
    Column(modifier = Modifier.fillMaxWidth()) {
        //1 titulo afuera
        Text(
            text = label,
            color = if(esError) Lleno else Neutro900,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium

        )
        Spacer(modifier = Modifier.height(6.dp))

        // 2. Caja de texto
        OutlinedTextField(
            value  = value,
            onValueChange = onValueChange,
            placeholder = {Text(text = placeholder, color = Neutro600)},
            leadingIcon = {
                Icon(imageVector = iconoIzquierdo, contentDescription = null)
            },
            trailingIcon = {
                if(iconoDerecho != null){
                    iconoDerecho()
                }
                else if(esCompletado && !esError){
                    Icon(imageVector = Icons.Default.Check, contentDescription = "Completado", tint = Disponible)
                }
            },
            isError = esError,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(12.dp),
            modifier = modifier.fillMaxWidth(),

            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Primario,
                unfocusedBorderColor = Neutro600,
                errorBorderColor = Lleno,
                focusedLeadingIconColor = Primario,
                unfocusedLeadingIconColor = Neutro600,
                errorLeadingIconColor = Lleno,
                focusedPlaceholderColor = Neutro600,
                unfocusedPlaceholderColor = Neutro600
            )
        )
        if(esError){
            Text(
                text = mensajeError,
                color = Lleno,
                modifier = Modifier.padding(top = 4.dp, start = 4.dp)
            )
        }
    }
}