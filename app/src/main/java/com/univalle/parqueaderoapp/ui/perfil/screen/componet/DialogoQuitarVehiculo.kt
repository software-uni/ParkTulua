package com.univalle.parqueaderoapp.ui.perfil.screen.componet

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.univalle.parqueaderoapp.ui.perfil.screen.VehiculoUiModel

@Composable
fun DialogoQuitarVehiculo(
    vehiculo: VehiculoUiModel,
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    val nombreVehiculo = vehiculo.placa?.uppercase() ?: vehiculo.tipo.lowercase()

    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("¿Quitar vehículo?") },
        text = {
            Text(
                "Tu $nombreVehiculo dejará de aparecer en tu perfil. " +
                        "Podrás volver a registrarlo más adelante."
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Quitar", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}