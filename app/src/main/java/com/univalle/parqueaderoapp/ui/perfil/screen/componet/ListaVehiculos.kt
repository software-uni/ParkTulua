package com.univalle.parqueaderoapp.ui.perfil.screen.componet

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.TwoWheeler
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    onAgregarVehiculoClick: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mis vehículos",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Neutro900
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
                    color = Primario
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
                    TarjetaVehiculo(vehiculo = vehiculo)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            OutlinedButton(
                onClick = onAgregarVehiculoClick,
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Primario // <-- Cambio aquí (opcional, para que el icono tome el color principal)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Agregar otro vehículo",
                    color = Primario // <-- Cambio aquí
                )
            }
        }
    }
}
@Composable
private fun TarjetaVehiculo(vehiculo: VehiculoUiModel) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = FondoApp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Si en el texto dice "Moto", pone el icono de moto, si no, el de carro
            val icono = if (vehiculo.tipo.contains("Moto", ignoreCase = true)) {
                Icons.Default.TwoWheeler
            } else {
                Icons.Default.DirectionsCar
            }

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(Neutro0, shape = RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icono,
                    contentDescription = "Icono de vehículo",
                    tint = Neutro600
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = vehiculo.placa.uppercase(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Neutro900
                )
                Text(
                    text = vehiculo.tipo,
                    fontSize = 14.sp,
                    color = Neutro600
                )
            }
        }
    }
}
@Composable
private fun EstadoVacioVehiculos(onAgregarVehiculoClick: () -> Unit) {
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
            tint = BordeSuave // <-- Cambio aquí
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