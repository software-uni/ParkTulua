package com.univalle.parqueaderoapp.ui.reserva.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.BordeSuave
import co.parktulua.core.designsystem.Neutro0
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioClaro

@Composable
fun TarjetaParqueaderoResumen(
    nombre : String,
    direccion : String,
    distancia : String,
    precioHora : String,
    modifier : Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Neutro0)
            .border(1.dp, BordeSuave, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        //Icono "P"
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(PrimarioClaro),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "P",
                color = Primario,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.width(12.dp))

        // Textos centrales
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = nombre,
                color = Neutro900,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "$direccion · $distancia",
                color = Neutro600,
                fontSize = 12.sp
            )
        }

        // Precio a la derecha
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = precioHora,
                color = Neutro900,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "por hora",
                color = Neutro600,
                fontSize = 12.sp
            )
        }
    }
}
@Preview(showBackground = true, backgroundColor = 0xFFF5F5F5) // Fondo gris claro para que resalte la tarjeta blanca
@Composable
fun TarjetaParqueaderoResumenPreview() {
    Box(modifier = Modifier.padding(16.dp)) {
        TarjetaParqueaderoResumen(
            nombre = "Parqueadero Central",
            direccion = "Cra. 24 #26-14",
            distancia = "350 m",
            precioHora = "$3.500"
        )
    }
}