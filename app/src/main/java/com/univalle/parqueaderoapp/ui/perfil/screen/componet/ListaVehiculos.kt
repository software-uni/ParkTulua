package com.univalle.parqueaderoapp.ui.perfil.screen.componet

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.Acento
import co.parktulua.core.designsystem.BordeSuave
import co.parktulua.core.designsystem.FondoApp
import co.parktulua.core.designsystem.Neutro0
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioClaro
import com.univalle.parqueaderoapp.ui.perfil.screen.VehiculoUiModel


@Composable
fun ListaVehiculos(
    vehiculos: List<VehiculoUiModel>,
    cantidad: Int,
    mostrarEstadoVacio: Boolean,
    onAgregarVehiculoClick: () -> Unit,
    onEliminarVehiculoClick: (VehiculoUiModel) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis vehículos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Neutro600
            )

            // Pequeño badge con el número total de vehículos
            Box(
                modifier = Modifier
                    .background(PrimarioClaro, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$cantidad",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Acento
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (mostrarEstadoVacio) {
            EstadoVacioVehiculos(onAgregarVehiculoClick = onAgregarVehiculoClick)
        } else {
            // Dibuja cada vehículo en la lista
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                vehiculos.forEach { vehiculo ->
                    TarjetaVehiculo(
                        vehiculo = vehiculo,
                        onEliminarClick ={onEliminarVehiculoClick(vehiculo)})
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = onAgregarVehiculoClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                border = BorderStroke(1.dp, Primario)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Primario
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Agregar otro vehículo",
                    color = Primario
                )
            }
        }
    }
}

@Composable
private fun TarjetaVehiculo(
    vehiculo: VehiculoUiModel,
    onEliminarClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.5.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Si en el texto dice "Moto", pone el icono de moto, si no, el de carro
            val icono = when {
                vehiculo.tipo.contains("Moto", ignoreCase = true) -> Icons.Default.TwoWheeler
                vehiculo.tipo.contains("Bici", ignoreCase = true) -> Icons.Default.DirectionsBike
                else -> Icons.Default.DirectionsCar
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(PrimarioClaro, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = "Icono de vehículo",
                    tint = Neutro600
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                vehiculo.placa?.let { placa ->
                    Text(
                        text = vehiculo.placa.uppercase(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Neutro900
                    )
                }
                Text(
                    text = vehiculo.tipo,
                    fontSize = 14.sp,
                    color = Neutro600
                )
            }
            IconButton(
                onClick = onEliminarClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Quitar vehículo",
                    tint = Neutro600
                )
            }
        }
    }
}


@Composable
private fun EstadoVacioVehiculos(
    onAgregarVehiculoClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.DirectionsCar,
            contentDescription = "Cero vehículos",
            modifier = Modifier.size(64.dp),
            tint = BordeSuave
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Aún no tienes vehículos",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Neutro900
        )
        Text(
            text = "Agrega tu primer vehículo para empezar a reservar en los parqueaderos.",
            fontSize = 14.sp,
            color = Neutro600,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 32.dp, vertical = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onAgregarVehiculoClick,
            colors = ButtonDefaults.buttonColors(containerColor = Primario),
            modifier = Modifier.height(48.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Agregar vehículo")
        }
    }
}