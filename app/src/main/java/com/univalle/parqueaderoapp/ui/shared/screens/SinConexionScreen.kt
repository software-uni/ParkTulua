package com.univalle.parqueaderoapp.ui.shared.screens

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
import androidx.compose.material.icons.outlined.CloudOff
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
fun SinConexionScreen(
    onReintentarClick : () -> Unit
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

        //circulo morado del icono
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(PrimarioClaro)
        ){
            Icon(
                imageVector = Icons.Outlined.CloudOff,
                contentDescription = "Sin conexión ",
                tint = Primario,
                modifier = Modifier.size(48.dp)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))

        //Textos normales
        Text(
            text = "Sin conexión",
            color = Neutro900,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Revisa tu internet para ver los\nparqueaderos y su disponibilidad.",
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
            text = "Se actualizará solo cuando vuelva la conexión",
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
private fun SinConexionScreenPreview() {
    SinConexionScreen(
        onReintentarClick = {}
    )
}