package com.univalle.parqueaderoapp.ui.reserva.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioClaro

@Composable
fun CronometroReserva(
    tiempoRestanteSegundos: Int,
    tiempoTotalSegundos: Int = 900 // Ejemplo: 15 minutos = 900 segundos
) {
    // Calcula el porcentaje de la barra (de 0.0 a 1.0)
    val progreso = tiempoRestanteSegundos.toFloat() / tiempoTotalSegundos.toFloat()

    // Formatea los segundos restantes a formato MM:SS
    val minutos = tiempoRestanteSegundos / 60
    val segundos = tiempoRestanteSegundos % 60
    val tiempoFormateado = String.format("%02d:%02d", minutos, segundos)

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(220.dp) // Tamaño general del reloj
    ) {
        // 1. El círculo de progreso
        CircularProgressIndicator(
            progress = { progreso },
            modifier = Modifier.size(220.dp),
            color = Primario,           // El morado oscuro de la barra que avanza
            trackColor = PrimarioClaro, // El morado clarito del fondo del círculo
            strokeWidth = 14.dp,        // Grosor de la línea
            strokeCap = StrokeCap.Round // Puntas redondeadas idénticas al diseño
        )

        // 2. El texto en el centro
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = tiempoFormateado,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Neutro900
            )
            Text(
                text = "minutos para llegar",
                fontSize = 14.sp,
                color = Neutro600
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun CronometroReservaPreview() {
    // Le pasamos 581 segundos para que en la vista previa
    // muestre exactamente "09:41" como en tu diseño de Figma (581 / 60 = 9 mins, sobran 41 segs)
    CronometroReserva(
        tiempoRestanteSegundos = 581,
        tiempoTotalSegundos = 900
    )
}