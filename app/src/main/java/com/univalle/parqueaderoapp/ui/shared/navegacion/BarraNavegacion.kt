package com.univalle.parqueaderoapp.ui.shared.navegacion

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.Disponible
import co.parktulua.core.designsystem.Neutro0
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioClaro
import com.univalle.parqueaderoapp.R
import com.univalle.parqueaderoapp.ui.navigation.PestanaNavegacion

@Composable
fun BarraNavegacion(
    pestanaActiva: PestanaNavegacion,
    onPestanaClick: (PestanaNavegacion) -> Unit,
    modifier: Modifier = Modifier,
    mostrarPuntoReserva: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp)
            .background(Neutro0)
            .navigationBarsPadding()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PestanaNavegacion.entries.forEach { pestana ->
            Pestana(
                pestana = pestana,
                activa = pestana == pestanaActiva,
                mostrarPunto = pestana == PestanaNavegacion.RESERVAS && mostrarPuntoReserva,
                onClick = { onPestanaClick(pestana) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun Pestana(
    pestana: PestanaNavegacion,
    activa: Boolean,
    mostrarPunto: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (activa) Primario else Neutro600

    Column(
        modifier = modifier
            .heightIn(min = 48.dp)
            .selectable(selected = activa, role = Role.Tab, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(width = 56.dp, height = 32.dp)
                .background(
                    if (activa) PrimarioClaro else Color.Transparent,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(pestana.icono()),
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            if (mostrarPunto) {
                Box(
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 2.dp, end = 10.dp)
                        .size(8.dp)
                        .background(Disponible, CircleShape)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = pestana.etiqueta,
            color = color,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontWeight = if (activa) FontWeight.SemiBold else FontWeight.Medium
        )
    }
}

@DrawableRes
private fun PestanaNavegacion.icono(): Int = when (this) {
    PestanaNavegacion.MAPA -> R.drawable.ic_mapa
    PestanaNavegacion.RESERVAS -> R.drawable.ic_reservas
    PestanaNavegacion.PERFIL -> R.drawable.ic_perfil
}