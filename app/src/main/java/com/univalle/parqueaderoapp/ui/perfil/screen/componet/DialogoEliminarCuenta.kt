package com.univalle.parqueaderoapp.ui.perfil.screen.componet

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun DialogoEliminarCuenta(
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        title = { Text("¿Eliminar tu cuenta?") },
        text = {
            Text(
                "Se borrarán tu nombre, correo, foto y vehículos. " +
                        "Esta acción no se puede deshacer. " +
                        "Si tienes una reserva activa, cancélala antes."
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text("Eliminar", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text("Cancelar")
            }
        }
    )
}