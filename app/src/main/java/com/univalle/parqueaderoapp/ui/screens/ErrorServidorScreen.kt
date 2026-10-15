package com.univalle.parqueaderoapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.FondoApp
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioClaro
import com.univalle.parqueaderoapp.ui.componet.botones.botones.BotonPrincipal

@Composable
fun ErrorServidorScreen(
    onReintentarClick : () -> Unit,
    mensajeError : String = "Estamos expreimentando dificultades tecnicas .\n or favor, intenta de nuevo más tarde."
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoApp)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        //Circulo de advertencia
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(1120.dp)
                .clip(CircleShape)
                .background(PrimarioClaro)
        ) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = "Error del servidor",
                tint = Primario,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "¡Ups! Algo salió mal",
            color = Neutro900,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = mensajeError,
            color = Neutro600,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        BotonPrincipal(
            texto = "Reintentar",
            onClick = onReintentarClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Si el problema persiste, contacta a soporte",
            color = Neutro600,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}
@Preview(showBackground = true)
@Composable
private fun ErrorServidorScreenPreview() {
    ErrorServidorScreen(onReintentarClick = {})
}