package com.univalle.parqueaderoapp.ui.vehiculo.components


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsCarFilled
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.Acento
import co.parktulua.core.designsystem.BordeSuave
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioClaro
import com.univalle.parqueaderoapp.ui.vehiculo.screen.TipoVehiculo

@Composable
fun SelectorTipoVehiculo(
    tipoSeleccionado : TipoVehiculo?,
    onSeleccionar: (TipoVehiculo) -> Unit
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        //Recorremos los valores del enum
        TipoVehiculo.entries.forEach { tipo ->
            val esSeleccionado = tipo == tipoSeleccionado

            val offsetX = remember { Animatable(0f) }
            LaunchedEffect(esSeleccionado) {
                if (esSeleccionado) {
                    // Secuencia rápida de movimiento horizontal
                    offsetX.animateTo(6f, animationSpec = tween(40))
                    offsetX.animateTo(-6f, animationSpec = tween(40))
                    offsetX.animateTo(4f, animationSpec = tween(40))
                    offsetX.animateTo(-4f, animationSpec = tween(40))
                    offsetX.animateTo(0f, animationSpec = tween(40))
                }
            }
            val icono: ImageVector = when (tipo) {
                TipoVehiculo.AUTOMOVIL -> Icons.Default.DirectionsCarFilled
                TipoVehiculo.MOTOCICLETA -> Icons.Default.TwoWheeler
                TipoVehiculo.BICICLETA -> Icons.Default.DirectionsBike
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .offset(x = offsetX.value.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onSeleccionar(tipo) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (esSeleccionado) PrimarioClaro else Color.White
                ),
                border = BorderStroke(
                    width = if (esSeleccionado) 1.5.dp else 1.dp,
                    color = if (esSeleccionado) Acento else BordeSuave
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = if (esSeleccionado) 2.dp else 0.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = tipo.nombre,
                        modifier = Modifier.size(32.dp),
                        tint = if (esSeleccionado) Acento else Neutro600
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tipo.nombre.replaceFirstChar { it.uppercase() },
                        fontSize = 12.sp,
                        fontWeight = if (esSeleccionado) FontWeight.Bold else FontWeight.Medium,
                        color = if (esSeleccionado) Acento else Neutro900,
                        maxLines = 1
                    )
                }
            }
        }
    }
}