package com.univalle.parqueaderoapp.ui.perfil.screen.componet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.parktulua.core.designsystem.LlenoTexto
import co.parktulua.core.designsystem.Neutro600
import co.parktulua.core.designsystem.Neutro900
import co.parktulua.core.designsystem.Primario
import co.parktulua.core.designsystem.PrimarioClaro

import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage

@Composable
fun EncabezadoPerfil(
    nombreCompleto: String,
    correo: String,
    fotoUrl: String? = null,
    onEditarClick: () -> Unit,
    onCerrarSesionClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- 1. AVATAR CON FOTO O INICIALES ---
        Box(
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape)
                .background(PrimarioClaro),
            contentAlignment = Alignment.Center
        ) {
            if (!fotoUrl.isNullOrBlank()) {
                SubcomposeAsyncImage(
                    model = fotoUrl,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    error = { InicialesAvatar(nombreCompleto) }
                )
            } else {
                Text(
                    text = obtenerIniciales(nombreCompleto),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primario // Morado principal
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 2. DATOS DEL USUARIO ---
        Text(
            text = nombreCompleto.ifBlank { "Usuario Invitado" },
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Neutro900
        )
        Text(
            text = correo.ifBlank { "Sin correo registrado" },
            fontSize = 14.sp,
            color = Neutro600
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            OutlinedButton(
                onClick = onEditarClick,
                modifier = Modifier.height(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar perfil",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Editar")
            }

            Spacer(modifier = Modifier.width(16.dp))

            TextButton(
                onClick = onCerrarSesionClick,
                colors = ButtonDefaults.textButtonColors(contentColor = LlenoTexto),
                modifier = Modifier.height(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Cerrar sesión",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Cerrar sesión")
            }
        }
    }
}

private fun obtenerIniciales(nombre: String): String {
    if (nombre.isBlank()) return "?"
    val palabras = nombre.trim().split(" ").filter { it.isNotBlank() }
    return when (palabras.size) {
        0 -> "?"
        1 -> palabras[0].take(2).uppercase()
        else -> "${palabras[0].first()}${palabras[1].first()}".uppercase()
    }
}
@Composable
private fun InicialesAvatar(nombre: String) {
    Text(
        text = obtenerIniciales(nombre),
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        color = Primario
    )
}